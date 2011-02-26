

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Pull[A](_1: Seq[_], _2: Iter[A]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        val it = _2.ator

        _1.onEnter { p =>
            f.enter(p)
            if (!it.hasNext) {
                f.exit(Exit.End)
            }
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
