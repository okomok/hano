

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.collection.mutable.Builder


private[hano]
class BoundedBuffer[A](capacity: Int) extends java.lang.Iterable[A] {
    private[this] val impl = new java.util.ArrayList[A](capacity)

    def isFull: Boolean = impl.size == capacity

    def isEmpty: Boolean = impl.isEmpty

    def addLast(x: A) {
        assert(!isFull)
        impl.add(x)
    }

    def removeFirst() {
        impl.remove(0)
    }

    def clear() {
        impl.clear()
    }

    override def iterator = impl.iterator
}
