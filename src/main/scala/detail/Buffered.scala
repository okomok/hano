

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.collection.mutable.Builder


private[hano]
class Buffered[A, To](_1: Seq[A], _2: Int, _3: () => Builder[A, To]) extends SeqAdapter.Of[To](_1) {
    Require.positive(_2, "buffered count")

    override def forloop(f: Reaction[To]) {
        val buf = new BoundedBuffer[A](_2)

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                buf.addLast(x)
                if (buf.isFull) {
                    f(Util.build(buf, _3()))
                    buf.clear()
                }
            }
        } onExit {
            case Exit.Success => {
                if (!buf.isEmpty) {
                    f(Util.build(buf, _3()))
                }
                f.exit()
            }
            case q => f.exit(q)
        } start()
    }
}


private[hano]
class BufferedFor[A, To](_1: Seq[A], _2: Long, _3: () => Builder[A, To]) extends SeqAdapter.Of[To](_1) {
    Require.nonnegative(_2, "bufferedFor duration")

    override def forloop(f: Reaction[To]) {
        val buf = new java.util.ArrayList[A]
        var past = 0L

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                buf.add(x)
                val now = System.currentTimeMillis
                if (now - past >= _2) {
                    past = now
                    f(Util.build(buf, _3()))
                    buf.clear()
                }
            }
        } onExit {
            case Exit.Success => {
                if (!buf.isEmpty) {
                    f(Util.build(buf, _3()))
                }
                f.exit()
            }
            case q => f.exit(q)
        } start()
    }
}
