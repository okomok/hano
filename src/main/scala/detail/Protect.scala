

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Protect[A](_1: Seq[A]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.onEnter { _ =>
            f.enter()
        } onEach {
            f(_)
        } onExit {
            f.exit(_)
        } start()
    }
}
