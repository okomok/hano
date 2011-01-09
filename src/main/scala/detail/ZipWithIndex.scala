

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ZipWithIndex[A](_1: Seq[A]) extends Seq[(A, Int)] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[(A, Int)]) {
        var i = 0
        _1 `for` { x =>
            f(x, i)
            i += 1
        } exit {
            f.exit(_)
        }
    }
}
