

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Once[A](_1: Seq[A]) extends SeqAdapter[A] with SeqOnce[A] {
    override protected val underlying = _1
    override def forloopOnce(f: Reaction[A]) = _1.forloop(f)
}
