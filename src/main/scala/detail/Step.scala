

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Step[A](_1: Seq[A], _2: Int) extends SeqProxy[A] {
    Require.positive(_2, "step count")

    override val self = _1.foldFilter(0) { (z, _) =>
        def newz = if (z + 1 == _2) 0 else (z + 1)
        if (z == 0) {
            Some((newz, true))
        } else {
            Some((newz, false))
        }
    }

    override def step(n: Int): Seq[A] = _1.step(_2 * n) // step.step fusion
}


private[hano]
class StepFor[A](_1: Seq[A], _2: Long) extends SeqProxy[A] {
    Require.nonnegative(_2, "stepFor duration")

    override val self = _1.foldFilter(0L) { (past, _) =>
        val now = System.currentTimeMillis
        if (now - past >= _2) {
            Some((now, true))
        } else {
            Some((past, false))
        }
    }

    override def stepFor(d: Long): Seq[A] = _1.stepFor(_2 * d) // stepFor.stepFor fusion
}
