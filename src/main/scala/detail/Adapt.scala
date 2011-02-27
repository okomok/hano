

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Adapt[A](_1: Seq[A], _2: (Seq[A], Reaction[A]) => Unit) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) = _2(_1, f)
}
