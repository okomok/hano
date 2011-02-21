

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop[A](_1: Seq[A]) extends SeqProxy[A] {
    override val self: Seq[A] = new Times(_1, IntVar.Inf)
}
