

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Drop[A](_1: Seq[A], _2: Int) extends SeqAdapter.Of[A](_1) {
    Require.nonnegative(_2, "drop count")

    override def forloop(f: Reaction[A]) {
        var c = _2
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (c == 0) {
                f(x)
            } else {
                c -= 1
            }
        } onExit {
            f.exit(_)
        } start()
    }

    override def drop(n: Int): Seq[A] = _1.drop(_2 + n) // drop.drop fusion
}
