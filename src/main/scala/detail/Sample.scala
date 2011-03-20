

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Sample[A](_1: Seq[A], _2: Iter[Boolean]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        val it = _2.ator

        _1.onEnter { p =>
            f.enter(p)
            if (!it.hasNext) {
                f.exit()
            }
        } onEach { x =>
            f.beforeExit {
                if (it.next) {
                    f(x)
                    if (!it.hasNext) {
                        f.exit()
                    }
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
