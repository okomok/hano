

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Sum[A, B >: A](_1: Seq[A], _2: Numeric[B]) extends SeqProxy[B] {
    override val self = _1.foldLeft(_2.zero)(_2.plus)
}
