

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// step 0 is meaningful?

private[hano]
class Step[A](_1: Seq[A], _2: Int) extends SeqAdapter.Class[A](_1) {
    Pre.positive(_2, "step")

    override def forloop(f: Reaction[A]) {
        var c = 0

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (c == 0) {
                f(x)
            }
            c += 1
            if (c == _2) {
                c = 0
            }
        } onExit {
            f.exit(_)
        } start()
    }

    override def step(n: Int): Seq[A] = _1.step(_2 * n) // step.step fusion
}


private[hano]
class StepTime[A](_1: Seq[A], _2: Long) extends SeqAdapter.Class[A](_1) {
    override def forloop(f: Reaction[A]) {
        var past = 0L

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            val now = System.currentTimeMillis
            if (now - past >= _2) {
                past = now
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }

    override def stepTime(i: Long): Seq[A] = _1.stepTime(_2 * i) // stepTime.stepTime fusion
}
