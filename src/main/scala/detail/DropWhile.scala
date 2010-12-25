

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class DropWhile[A](_1: Seq[A], _2: A => Boolean) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: Reaction[A]) {
        var go = false
        For(_1) { x =>
            if (!go && !_2(x)) {
                go = true
            }
            if (go) {
                f(x)
            }
        } AndThen {
            f.exit(_)
        }
    }
}
