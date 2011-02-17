

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Indices[A](_1: Seq[A]) extends SeqProxy[Int] {
    override val self = _1.pull((0 until java.lang.Integer.MAX_VALUE).view)
}
