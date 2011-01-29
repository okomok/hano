

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Generate[A](_1: Seq[_], _2: => Iter[A]) extends Seq[A] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        val _k = ExitOnce { q => close(); f.exit(q) }

        val it = _2.ator
        if (!it.hasNext) {
            context.eval {
                _k(Exit.End)
            }
        } else {
            _1 onEach { _ =>
                if (it.hasNext) {
                    f(it.next)
                    if (!it.hasNext) {
                        _k(Exit.End)
                    }
                }
            } onExit {
                _k(_)
            } start()
        }
    }
}
