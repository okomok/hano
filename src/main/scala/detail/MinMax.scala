

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Min[A, B >: A](_1: Seq[A], _2: Ordering[B]) extends SeqProxy[A] {
    override val self = _1.reduceLeft((x, y) => if (_2.lteq(x, y)) x else y)
}

private[hano]
class Max[A, B >: A](_1: Seq[A], _2: Ordering[B]) extends SeqProxy[A] {
    override val self = _1.reduceLeft((x, y) => if (_2.gteq(x, y)) x else y)
}


private[hano]
class MinBy[A, B](_1: Seq[A], _2: A => B, _3: Ordering[B]) extends SeqProxy[A] {
    override val self = _1.reduceLeft((x, y) => if (_3.lteq(_2(x), _2(y))) x else y)
}

private[hano]
class MaxBy[A, B](_1: Seq[A], _2: A => B, _3: Ordering[B]) extends SeqProxy[A] {
    override val self = _1.reduceLeft((x, y) => if (_3.gteq(_2(x), _2(y))) x else y)
}
