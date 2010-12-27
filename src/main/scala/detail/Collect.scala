

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Collect[A, B](_1: Seq[A], _2: PartialFunction[A, B]) extends SeqProxy[B] {
    override val self = _1.filter(_2.isDefinedAt).map(_2)
}
