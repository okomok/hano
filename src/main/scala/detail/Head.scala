

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Head[A](_1: Seq[A]) extends SeqAdapter.Of[A](_1) with SingleSeq[A] {
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f(x)
            f.exit(Exit.Success)
        } onExit {
            case Exit.Success => f.exit(Exit.Failure(new NoSuchElementException("aSeq.head")))
            case q => f.exit(q)
        } start()
    }
}
