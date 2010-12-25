

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// step 0 is meaningful?

private[hano]
class Step[A](_1: Seq[A], _2: Int) extends Seq[A] {
    Pre.positive(_2, "step")

    override def close() = _1.close()
    override def forloop(f: Reaction[A]) {
        var c = 0
        For(_1) { x =>
            if (c == 0) {
                f(x)
            }
            c += 1
            if (c == _2) {
                c = 0
            }
        } AndThen {
            f.exit(_)
        }
    }

    override def step(n: Int): Seq[A] = _1.step(_2 * n) // step.step fusion
}


private[hano]
class StepTime[A](_1: Seq[A], _2: Long) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: Reaction[A]) {
        var past = 0L
        For(_1) { x =>
            val now = System.currentTimeMillis
            if (now - past >= _2) {
                past = now
                f(x)
            }
        } AndThen {
            f.exit(_)
        }
    }

    override def stepTime(i: Long): Seq[A] = _1.stepTime(_2 * i) // stepTime.stepTime fusion
}
