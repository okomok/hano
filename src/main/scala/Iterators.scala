

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Contains methods missing from the standard.
 */
object Iterators {
    /**
     * Creates an `Iterable` from an `Iterator`.
     */
    def toIterable[A](it: => Iterator[A]): Iterable[A] = new ToIterable(() => it)

    /**
     * The dual of foldRight
     */
    def unfold[A, B](z: A)(op: A => Option[(B, A)]): Iterator[B] = new Unfold(z, op).concrete

    /**
     * Cycles an `Iterable` indefinitely.
     */
    def cycle[A](it: => Iterator[A]): Iterator[A] = Iterator.continually(()).flatMap(_ => it)

    /**
     * An infinite sequence of the current times.
     */
    def currentDate: Iterator[java.util.Date] = Iterator.continually(new java.util.Date)


    private class ToIterable[A](_1: () => Iterator[A]) extends Iterable[A] {
        override def iterator = _1()
    }

    private class Unfold[A, +B](_1: A, _2: A => Option[(B, A)]) extends detail.AbstractIterator[B] {
        private[this] var _acc = _2(_1)
        override def isEnd = _acc.isEmpty
        override def deref = _acc.get._1
        override def increment() = _acc = _2(_acc.get._2)
    }
}
