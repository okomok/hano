

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class React[A](_1: Seq[A], _2: Reaction[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: Reaction[A]) {
        For(_1) { x =>
            _2(x)
            f(x)
        } AndThen { q =>
            _2.onExit(q)
            f.onExit(q)
        }
    }
}
