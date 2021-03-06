

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// BROKEN: _2 shall be Iter.

private[hano]
class Unsplit[A](_1: Seq[Seq[A]], _2: Seq[A]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        var first = true

        _1.onEnter {
            f.enter(_)
        } onEach { s =>
            if (first) {
                first = false
            } else {
                for (x <- _2) {
                    f(x)
                }
            }
            for (x <- s) {
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
