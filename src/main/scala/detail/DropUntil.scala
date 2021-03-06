

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class DropUntil[A](_1: Seq[A], _2: Seq[_]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        @volatile var go = false

        _2.head.onEach { _ =>
            go = true
        } start()

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (go) {
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
