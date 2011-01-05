

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Shift[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = _1.close()
    override def context = _2.context
    override def forloop(f: Reaction[A]) {
        For(_1) { x =>
            context.eval {
                f(x)
            }
        } AndThen { q =>
            context.eval {
                f.exit(q)
            }
        }
    }
}
