

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Init[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: Reaction[A]) {
        var prev: Option[A] = None
        For(_1) { x =>
            if (!prev.isEmpty) {
                f(prev.get)
            }
            prev = Some(x)
        } AndThen {
            f.exit(_)
        }
    }
}
