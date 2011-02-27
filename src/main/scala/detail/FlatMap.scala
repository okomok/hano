

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// No End sequence
private[hano]
class FlatMap[A, B](_1: Seq[A], _2: A => Seq[B]) extends Seq[B] {
    override val context = _1.context.toKnown

    override def forloop(f: Reaction[B]) {
        _1.noEnd.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                _2(x).noEnd.shift {
                    _1
                } onEach {
                    f(_)
                } onExit {
                    f.exit(_)
                } start()
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
