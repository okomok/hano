

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Tail[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        var first = true
        _1 `for` { x =>
            if (first) {
                first = false
            } else {
                f(x)
            }
        } exit {
            f.exit(_)
        }
    }
}
