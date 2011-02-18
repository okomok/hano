

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Pull[A](_1: Seq[_], _2: Iter[A]) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        def _k(q: Exit) { close(); f.exit(q) }

        val it = _2.ator
        if (!it.hasNext) {
            context eval {
                _k(Exit.End)
            }
        } else {
            _1 onEach { _ =>
                f beforeExit {
                    if (it.hasNext) {
                        f(it.next)
                        if (!it.hasNext) {
                            _k(Exit.End)
                        }
                    }
                }
            } onExit {
                _k(_)
            } start()
        }
    }
}
