

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Length[A](_1: Seq[A]) extends SeqProxy[Int] {
    override val self = _1.foldLeft(0)((a, _) => a + 1)
}
