

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Pull[A](_1: Seq[_], _2: Iter[A]) extends SeqProxy[A] {
    override val self = _1.zipWith(_2).map(_._2)
}
