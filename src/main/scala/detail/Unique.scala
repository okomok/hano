

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Unique[A, B >: A](_1: Seq[A], _2: Equiv[B]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        var prev: Option[A] = None

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                if (prev.isEmpty || !_2.equiv(prev.get, x)) {
                    f(x)
                }
                prev = Some(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
