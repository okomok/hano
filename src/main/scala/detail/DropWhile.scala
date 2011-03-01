

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class DropWhile[A](_1: Seq[A], _2: A => Boolean) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        var go = false

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (!go && !_2(x)) {
                go = true
            }
            if (go) {
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
