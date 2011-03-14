

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ZipWith[A, B](_1: Seq[A], _2: Iter[B]) extends SeqAdapter.Of[(A, B)](_1) {
    override def forloop(f: Reaction[(A, B)]) {
        val it = _2.ator

        def _exit(q: Exit.Status) {
            f.exit(q)
            Iterators.close(it)
        }

        _1.onEnter { p =>
            f.enter(p)
            if (!it.hasNext) {
                _exit(Exit.Success)
            }
        } onEach { x =>
            f.beforeExit {
                if (it.hasNext) {
                    f((x, it.next))
                    if (!it.hasNext) {
                        _exit(Exit.Success)
                    }
                }
            }
        } onExit { q =>
            _exit(q)
        } start()
    }
}
