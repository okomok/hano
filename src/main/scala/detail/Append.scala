

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Append[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        _1 `for` {
            f(_)
        } exit {
            case Exit.End =>
                _2.shift(_1) `for` {
                    f(_)
                } exit {
                    f.exit(_)
                }
            case q => f.exit(q)
        }
    }
}
