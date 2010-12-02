

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import hano.Annotation
import scala.collection.{Iterator, Iterable => Result, JavaConversions}


object Iterable {

    def apply[A](xs: A*): Result[A] = xs.view

    def from[A](that: Result[A]): Result[A] = that.view

    def from[A](from: java.lang.Iterable[A]): Result[A] = _Iterable.fromJIterable(from).view

    def emptyOf[A]: Result[A] = Result.empty.view

    def single[A](x: A): Result[A] = bind(Iterator.single(x))

    def bind[A](from: Iterator[A]): Result[A] = new Result[A] {
        override def iterator = from
    }.view

    def repeat[A](x: A): Result[A] = bind( new Iterator[A] {
        override def hasNext = true
        override def next = x
    } )

    def iterate[A](z: A)(f: A => A): Result[A] = bind(Iterator.iterate(z)(f))

    def lazySingle[A](x: => A): Result[A] = bind( new Iterator[A] {
        private[this] var hasnext = true
        override def hasNext = hasnext
        override def next = if (hasnext) { hasnext = false; x } else Iterator.empty.next
    } )

}

private object _Iterable {

    import JavaConversions._
    def fromJIterable[A](from: java.lang.Iterable[A]): Result[A] = from

}
