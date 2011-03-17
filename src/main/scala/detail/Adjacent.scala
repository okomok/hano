

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.collection.mutable.Builder


private[hano]
class Adjacent[A, To](_1: Seq[A], _2: Int, _3: () => Builder[A, To]) extends SeqAdapter.Of[To](_1) {
    Pre.positive(_2, "adjacent")

    override def forloop(f: Reaction[To]) {
        val buf = new BoundedBuffer[A](_2)

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                buf.addLast(x)
                if (buf.isFull) {
                    f(Util.build(buf, _3()))
                    buf.removeFirst()
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
