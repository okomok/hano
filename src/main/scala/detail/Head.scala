

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Head[A](_1: Seq[A]) extends SeqAdapter.Class[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f(x)
            f.exit(Exit.End)
        } onExit {
            case Exit.End => f.exit(Exit.Failed(new NoSuchElementException("Seq.head")))
            case q => f.exit(q)
        } start()
    }
}
