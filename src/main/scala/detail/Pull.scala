

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Pull[A](_1: Seq[_], _2: Iter[A]) extends SeqAdapter[A] {
    override protected val underlying = _1
    override val context = _1.context.toKnown
    override def forloop(f: Reaction[A]) {
        val it = _2.ator
        if (!it.hasNext) {
            _1.clear.forloop(f)
        } else {
            _1.onEnter {
                f.enter(_)
            } onEach { _ =>
                f.beforeExit {
                    if (it.hasNext) {
                        f(it.next)
                        if (!it.hasNext) {
                            f.exit(Exit.End)
                        }
                    }
                }
            } onExit {
                f.exit(_)
            } start()
        }
    }
}
