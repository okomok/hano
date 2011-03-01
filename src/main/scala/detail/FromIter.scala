

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class FromIter[A](_1: Iter[A]) extends Seq[A] {
    override def context = Self

    override def forloop(f: Reaction[A]) {
        @volatile var status: Exit.Status = Exit.Success
        @volatile var isActive = true

        f.enter {
            Exit { q =>
                status = Exit.Failure(Exit.ByOther(q))
                isActive = false
            }
        }

        f.applying {
            val it = _1.ator
            while (isActive && it.hasNext) {
                f(it.next)
            }
        }

        f.exit(status)
    }
}
