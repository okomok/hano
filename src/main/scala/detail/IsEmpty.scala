

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class IsEmpty(_1: Seq[_]) extends SeqAdapter[Boolean] {
    override protected val underlying = _1
    override def forloop(f: Reaction[Boolean]) {
        def _k(q: Exit) { close(); f.exit(q) }

        _1 onEach { _ =>
            f(false)
            _k(Exit.End)
        } onExit {
            case q @ Exit.End => {
                f(true)
                _k(q)
            }
            case q => _k(q)
        } start()
    }
}
