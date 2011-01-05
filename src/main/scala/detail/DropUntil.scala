

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class DropUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        @volatile var go = false
        lazy val g = _2.close()

        for (y <- _2.shift(_1)) {
            go = true
            g
        }

        For(_1) { x =>
            if (go) {
                g
                f(x)
            }
        } AndThen {
            f.exit(_)
        }
    }
}
