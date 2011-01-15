

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Single[A](_1: A) extends Seq[A] {
    override def context = Context.self
    override def forloop(f: Reaction[A]) {
        context `for` { _ =>
            f(_1)
        } exit {
            f.exit(_)
        }
    }
}
