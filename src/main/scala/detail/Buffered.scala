

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.collection.mutable.Builder


private[hano]
class Buffered[A, To](_1: Seq[A], _2: Int, _3: () => Builder[A, To]) extends SeqAdapter.Of[To](_1) {
    Pre.positive(_2, "buffered")

    override def forloop(f: Reaction[To]) {
        val buf = new WorkBuffer[A](_2)
        val b = _3()

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            buf.addLast(x)
            if (buf.isFull) {
                f(buf.copy(b))
                buf.removeFirst()
            }
        } onExit {
            f.exit(_)
        } start()
    }
}


private[hano]
class WorkBuffer[A](capacity: Int) {
    private[this] val impl = new java.util.ArrayList[A](capacity)

    def isFull: Boolean = impl.size == capacity

    def removeFirst() = impl.remove(0)

    def addLast(x: A) {
        assert(!isFull)
        impl.add(x)
    }

    def copy[To](b: Builder[A, To]): To = {
        b.clear()
        for (x <- Iter.from(impl).able) {
            b += x
        }
        b.result()
    }
}
