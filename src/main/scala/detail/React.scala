

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class React[A](_1: Seq[A], _2: () => Reaction[A]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        val g = _2()

        _1.onEnter {
            g.enter(_)
        } onEach {
            g(_)
        } onExit {
            g.exit(_)
        } forloop(f)
    }
}
