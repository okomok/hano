

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import hano.Annotation
import scala.collection.{Iterator, Iterable => Result, JavaConversions}


@deprecated("use Iter instead")
object Iterable {

    def apply[A](xs: A*): Result[A] = xs

    def from[A](that: Result[A]): Result[A] = that

    def from[A](from: java.lang.Iterable[A]): Result[A] = {
        import JavaConversions._
        from
    }

    def emptyOf[A]: Result[A] = Result.empty

    def single[A](x: A): Result[A] = bind(Iterator.single(x))

    def bind[A](from: Iterator[A]): Result[A] = new Result[A] {
        override def iterator = from
    }

    def repeat[A](x: A): Result[A] = bind( new Iterator[A] {
        override def hasNext = true
        override def next = x
    } ).view

    def iterate[A](z: A)(f: A => A): Result[A] = bind(Iterator.iterate(z)(f)).view

    def lazySingle[A](x: => A): Result[A] = {
        lazy val y = x
        bind( new Iterator[A] {
            private[this] var hasnext = true
            override def hasNext = hasnext
            override def next = if (hasnext) { hasnext = false; y } else Iterator.empty.next
        } )
    }

}
