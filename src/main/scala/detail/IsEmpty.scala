

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class IsEmpty(_1: Seq[_]) extends SeqAdapter[Boolean] {
    override protected val underlying = _1
    override def forloop(f: Reaction[Boolean]) {
        _1.onEnter {
            f.enter(_)
        } onEach { _ =>
            f(false)
            f.exit(Exit.End)
        } onExit {
            case q @ Exit.End => {
                f(true)
                f.exit(q)
            }
            case q => f.exit(q)
        } start()
    }
}
