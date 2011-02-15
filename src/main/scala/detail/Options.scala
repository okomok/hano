

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Options[A](_1: Seq[A]) extends SeqAdapter[Option[A]] {
    override protected val underlying = _1
    override def forloop(f: Reaction[Option[A]]) {
        _1 onEach { x =>
            f(Some(x))
        } onExit {
            case q @ Exit.End => f.exit(q)
            case q => {
                f(None)
                f.exit(Exit.End)
            }
        } start()
    }
}
