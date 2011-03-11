

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class FromIter[A](_1: Iter[A]) extends Seq[A] {
    override def process = Self

    override def forloop(f: Reaction[A]) {
        val loop = new Loop
        var it: Iterator[A] = null

        f.enter {
            Exit { q =>
                loop.end(q)
            }
        } applying {
            loop.begin()
            it = _1.ator
            while (loop.isActive && it.hasNext) {
                f(it.next)
            }
        } exit {
            if (it.hasNext) {
                loop.status
            } else {
                Exit.Success
            }
        }
    }
}
