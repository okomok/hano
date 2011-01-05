

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Breakable[A](_1: Seq[A]) extends Seq[(A, () => Unit)] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[(A, () => Unit)]) {
        For(_1) { x =>
            // Note f.exit in f.apply is illegal.
            f(x, () => close())
        } AndThen {
            f.exit(_)
        }
    }
}
