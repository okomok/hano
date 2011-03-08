

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ShiftStart[A](_1: Seq[A], _2: Seq[_]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        _2.process.eval {
            _1.forloop(f)
        }
    }
}
