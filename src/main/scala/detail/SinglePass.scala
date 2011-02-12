

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class SinglePass[A](_1: Seq[A]) extends Resource[A] {
    override def context = _1.context
    override def closeResource() = _1.close()
    override def openResource(f: Reaction[A]) = _1.forloop(f)
}
