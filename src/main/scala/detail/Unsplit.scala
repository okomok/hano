

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// BROKEN: _2 shall be Iter.

private[hano]
class Unsplit[A](_1: Seq[Seq[A]], _2: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        var first = true
        For(_1) { s =>
            if (first) {
                first = false
            } else {
                for (x <- _2) {
                    f(x)
                }
            }
            for (x <- s) {
                f(x)
            }
        } AndThen {
            f.exit(_)
        }
    }
}
