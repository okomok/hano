

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Protect[A](_1: Seq[A]) extends Seq[A] {
    // override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) = _1.forloop(f, k)
    override def protect: Seq[A] = _1.protect // protect.protect fusion
}
