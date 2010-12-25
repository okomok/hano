

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.collection.immutable.{IndexedSeq, Vector}
import scala.collection.JavaConversions


private[hano]
class Adjacent[A](_1: Seq[A], _2: Int) extends Seq[IndexedSeq[A]] {
    override def close() = _1.close()
    override def forloop(f: IndexedSeq[A] => Unit, k: Exit => Unit) {
        val buf = new AdjacentBuffer[A](_2)
        For(_1) { x =>
            buf.addLast(x)
            if (buf.isFull) {
                f(buf.toIndexedSeq)
                buf.removeFirst()
            }
        } AndThen {
            k
        }
    }
}


private[hano]
class AdjacentBuffer[A](capacity: Int) {
    Pre.positive(capacity, "adjacent")
    private[this] val impl = new java.util.ArrayList[A](capacity)
    def isFull: Boolean = impl.size == capacity
    def removeFirst(): Unit = impl.remove(0)
    def addLast(x: A) { assert(!isFull); impl.add(x) }
    def toIndexedSeq: IndexedSeq[A] = {
        import JavaConversions._
        Vector.empty ++ impl
    }
}
