

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Count[A](_1: Seq[A], _2: A => Boolean) extends SeqProxy[Int] {
    override val self = _1.foldLeft(0)((a, x) => if (_2(x)) a + 1 else a)
}
