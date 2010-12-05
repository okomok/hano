

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import scala.collection.JavaConversions
import hano.Annotation


object Iter {

    def apply[A](xs: A*): Iter[A] = from(xs)

    @Annotation.returnThat
    def from[A](that: Iter[A]): Iter[A] = that

    implicit def fromIterator[A](from: => Iterator[A]): Iter[A] = new Iter[A] {
        override def begin = from
    }

    implicit def fromIterable[A](from: Iterable[A]): Iter[A] = new Iter[A] {
        override def begin = from.iterator
    }

    implicit def fromJIterator[A](from: => java.util.Iterator[A]): Iter[A] = new Iter[A] {
        import JavaConversions._
        override def begin = from
    }

    implicit def fromJIterable[A](from: java.lang.Iterable[A]): Iter[A] = new Iter[A] {
        import JavaConversions._
        override def begin = from.iterator
    }

    implicit def fromCursor[A](from: => Cursor[A]): Iter[A] = new Iter[A] {
        override def begin = from.toIterator
    }

    def lazySingle[A](x: => A): Iter[A] = {
        lazy val y = x
        new Iterator[A] {
            private[this] var hasnext = true
            override def hasNext = hasnext
            override def next = if (hasnext) { hasnext = false; y } else Iterator.empty.next
        }
    }

}


/**
 * Trivial wrapper for Iterators
 */
trait Iter[+A] extends Equals {

    @Annotation.returnThis
    def of[B >: A]: Iter[B] = this

    def begin: Iterator[A]

    def able = new Iterable[A] {
        override def iterator = begin
    }

    def isEmpty: Boolean = begin.isEmpty

    def length: Int = begin.length

    def foreach(f: A => Unit): Unit = begin.foreach(f)

    @Annotation.pre("finite if result is `true`")
    override def equals(that: Any) = that match {
        case that: Iter[_] => (that canEqual this) && equalsIf(that)(_ == _)
        case _ => false
    }

    override def canEqual(that: Any) = that.isInstanceOf[Iter[_]]

    @Annotation.pre("finite")
    override def hashCode = {
        var r = 1
        val it = begin
        while (it.hasNext) {
            r = 31 * r + it.next.hashCode
        }
        r
    }

    @Annotation.pre("finite")
    override def toString = {
        val sb = new StringBuilder
        sb.append('[')

        val it = begin
        if (it.hasNext) {
            sb.append(it.next)
        }
        while (it.hasNext) {
            sb.append(", ")
            sb.append(it.next)
        }

        sb.append(']')
        sb.toString
    }

    def equalsIf[B](that: Iter[B])(p: (A, B) => Boolean): Boolean = {
        val it = begin
        val jt = that.begin
        while (it.hasNext && jt.hasNext) {
            if (!p(it.next, jt.next)) {
                return false
            }
        }
        !it.hasNext && !jt.hasNext
    }
}
