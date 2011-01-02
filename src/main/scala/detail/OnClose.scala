

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnClose[A](_1: Seq[A], _2: => Unit) extends Seq[A] {
    override def close() = { _2; _1.close() }
    override def forloop(f: Reaction[A]) = _1.forloop(f)
}
