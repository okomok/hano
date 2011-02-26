

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class DropUntil[A](_1: Seq[A], _2: Seq[_]) extends SeqAdapter.Class[A](_1) {
    override def forloop(f: Reaction[A]) {
        @volatile var go = false

        _2.onEnter { p =>
            go = true
            p.close()
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
