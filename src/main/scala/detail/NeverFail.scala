

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class NeverFail[A](_1: Seq[A]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach {
            f(_)
        } onExit { _ =>
            f.exit(Exit.Success)
        } start()
    }
}
