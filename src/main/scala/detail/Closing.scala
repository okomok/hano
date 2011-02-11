

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Closing[A](_1: Seq[A], _2: => Boolean) extends Seq[A] {
    override def close() {
        if (!_2) {
            _1.close()
        }
    }
    override def context = _1.context
    override def forloop(f: Reaction[A]) = _1.forloop(f)
}
