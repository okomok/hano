

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Step[A](_1: Seq[A], _2: Int) extends SeqProxy[A] {
    Require.positive(_2, "step count")

    override val self = _1.sample {
        var i = 0
        Iterator.continually {
            val res = i == 0
            i += 1
            if (i == _2) {
                i = 0
            }
            res
        }
    }

    override def step(n: Int): Seq[A] = _1.step(_2 * n) // step.step fusion
}


private[hano]
class StepFor[A](_1: Seq[A], _2: Long) extends SeqProxy[A] {
    Require.nonnegative(_2, "stepFor duration")

    override val self = _1.sample {
        var past = 0L
        Iterator.continually {
            val now = System.currentTimeMillis
            if (now - past >= _2) {
                past = now
                true
            } else {
                false
            }
        }
    }

    override def stepFor(d: Long): Seq[A] = _1.stepFor(_2 * d) // stepFor.stepFor fusion
}
