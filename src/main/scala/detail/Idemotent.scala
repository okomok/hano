

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Idempotent[A](_1: Seq[A]) extends SeqAdapter[A] {
    private[this] var go = true
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        if (go) {
            go = false
            _1.forloop(f)
        }
    }
}
