

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class IsEmpty(_1: Seq[_]) extends SeqAdapter.Of[Boolean](_1) with SingleSeq[Boolean] {
    override def forloop(f: Reaction[Boolean]) {
        _1.onEnter {
            f.enter(_)
        } onEach { _ =>
            f(false)
            f.exit()
        } onExit {
            case Exit.Success => {
                f(true)
                f.exit()
            }
            case q => f.exit(q)
        } start()
    }
}
