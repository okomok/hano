

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Min[A](_1: Seq[A], _2: Ordering[A]) extends SeqProxy[A] {
    override val self = _1.reduceLeft { (a, x) => if (_2.lt(a, x)) a else x }
}

private[hano]
class Max[A](_1: Seq[A], _2: Ordering[A]) extends SeqProxy[A] {
    override val self = _1.reduceLeft { (a, x) => if (_2.gt(a, x)) a else x }
}
