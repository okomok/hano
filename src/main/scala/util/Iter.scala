

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import scala.collection.JavaConversions


object Iter {

    def apply[A](xs: A*): Iterator[A] = xs.iterator

    def from[A](that: Iterator[A]): Iterator[A] = that

    def from[A](from: java.util.Iterator[A]): Iterator[A] = {
        import JavaConversions._
        from
    }

    def from[A](from: java.lang.Iterable[A]): Iterator[A] = {
        import JavaConversions._
        from.iterator
    }

    def emptyOf[A]: Iterator[A] = Iterator.empty

    def able[A](i: => Iterator[A]) = new Iterable[A] {
        override def iterator = i
    }

    def able[A](i: => java.util.Iterator[A])(implicit d: DummyImplicit): Iterable[A] = able(from(i))

    def lazySingle[A](x: => A): Iterator[A] = {
        lazy val y = x
        new Iterator[A] {
            private[this] var hasnext = true
            override def hasNext = hasnext
            override def next = if (hasnext) { hasnext = false; y } else Iterator.empty.next
        }
    }

}
