

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// No End sequence
private[hano]
class FlatMap[A, B](_1: Seq[A], _2: A => Seq[B]) extends SeqAdapter.Of[B](_1) {
    override def forloop(f: Reaction[B]) {
        _1.noSuccess.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                _2(x).noSuccess.shift {
                    process
                } forloop(f)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
