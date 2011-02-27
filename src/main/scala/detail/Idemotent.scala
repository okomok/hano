

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Idempotent[A](_1: Seq[A]) extends SeqAdapter.Of[A](_1) {
    private[this] var go = true

    override def forloop(f: Reaction[A]) {
        if (go) {
            go = false
            _1.forloop(f)
        }
    }
}
