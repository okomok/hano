

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Map[A, B](_1: Seq[A], _2: A => B) extends Seq[B] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[B]) {
        _1 onEach { x =>
            f(_2(x))
        } onExit {
            f.exit(_)
        } start()
    }
}
