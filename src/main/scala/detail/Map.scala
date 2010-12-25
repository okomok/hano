

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Map[A, B](_1: Seq[A], _2: A => B) extends Seq[B] {
    override def close() = _1.close()
    override def forloop(f: B => Unit, k: Exit => Unit) {
        For(_1) { x =>
            f(_2(x))
        } AndThen {
            k
        }
    }
}
