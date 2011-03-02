

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class FromIter[A](_1: Iter[A]) extends Seq[A] {
    override def context = Self

    override def forloop(f: Reaction[A]) {
        @volatile var status = Exit.Success.asStatus
        @volatile var isActive = true

        var it: Iterator[A] = null

        f.enter {
            Exit { q =>
                status = Exit.Failure(Exit.ByOther(q))
                isActive = false
            }
        } applying {
            it = _1.ator
            while (isActive && it.hasNext) {
                f(it.next)
            }
        } exit {
            if (it.hasNext) {
                status
            } else {
                Exit.Success
            }
        }
    }
}
