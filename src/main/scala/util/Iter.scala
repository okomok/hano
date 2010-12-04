

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import scala.collection.JavaConversions


object Iter {

    def apply[A](xs: A*): Iter[A] = from(xs)

    @hano.Annotation.returnThat
    def from[A](that: Iter[A]): Iter[A] = that

    implicit def fromSIterator[A](from: => scala.collection.Iterator[A]): Iter[A] = new Iter[A] {
        override def begin = from
    }

    implicit def fromSIterable[A](from: scala.collection.Iterable[A]): Iter[A] = new Iter[A] {
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

    def lazySingle[A](x: => A): Iter[A] = {
        lazy val y = x
        new scala.collection.Iterator[A] {
            private[this] var hasnext = true
            override def hasNext = hasnext
            override def next = if (hasnext) { hasnext = false; y } else Iterator.empty.next
        }
    }

}


/**
 * Trivial wrapper for Iterators
 */
sealed abstract class Iter[+A] {
    def begin: scala.collection.Iterator[A]
    def isEmpty: Boolean = begin.isEmpty

    def able = new scala.collection.Iterable[A] {
        override def iterator = begin
    }

    def foreach(f: A => Unit): Unit = begin.foreach(f)
}
