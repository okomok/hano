

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class FoldFilter[A, B](_1: Seq[A], _2: B, _3: (B, A) => Option[(B, Boolean)]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        var acc = _2

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                _3(acc, x) match {
                    case Some((z, true)) => acc = z; f(x)
                    case Some((z, false)) => acc = z
                    case None => f.exit()
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
