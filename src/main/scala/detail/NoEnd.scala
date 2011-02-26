

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class NoEnd[A](_1: Seq[A]) extends SeqAdapter[A] {
    override protected val underlying = _1

    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach {
            f(_)
        } onExit {
            case Exit.End => ()
            case q => f.exit(q)
        } start()
    }
}
