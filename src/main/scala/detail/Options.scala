

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Options[A](_1: Seq[A]) extends SeqAdapter.Of[Option[A]](_1) {
    override def forloop(f: Reaction[Option[A]]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f(Some(x))
        } onExit {
            case Exit.Success => f.exit()
            case q => {
                f(None)
                f.exit()
            }
        } start()
    }
}
