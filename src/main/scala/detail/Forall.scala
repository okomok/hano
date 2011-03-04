

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Forall[A](_1: Seq[A], _2: A => Boolean) extends SeqProxy[Boolean] {
    override val self = _1.find(!_2(_)).option.map(_.isEmpty)
}
