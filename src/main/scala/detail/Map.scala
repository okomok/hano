

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Map[A, B](_1: Seq[A], _2: A => B) extends SeqAdapter.Of[B](_1) {
    override def forloop(f: Reaction[B]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f(_2(x))
        } onExit {
            f.exit(_)
        } start()
    }
}
