

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.collection.immutable.{IndexedSeq, Vector}


private class Adjacent[A](_1: Seq[A], _2: Int) extends Seq[IndexedSeq[A]] {
    override def close() = _1.close()
    override def forloop(f: IndexedSeq[A] => Unit, k: Exit => Unit) {
        val buf = new AdjacentBuffer[A](_2)
        _1 _for { x =>
            buf.addLast(x)
            if (buf.isFull) {
                f(buf.toIndexedSeq)
                buf.removeFirst()
            }
        } _andThen {
            k
        }
    }
}


private class AdjacentBuffer[A](capacity: Int) {
    Pre.positive(capacity, "adjacent")
    private[this] val impl = new java.util.ArrayList[A](capacity)
    def isFull: Boolean = impl.size == capacity
    def removeFirst(): Unit = impl.remove(0)
    def addLast(x: A) { assert(!isFull); impl.add(x) }
    def toIndexedSeq: IndexedSeq[A] = util.Vector.make(impl)
}
