

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Contains methods missing from the standard.
 */
object Iterators {
    /**
     * Creates an infinite-length iterator returning `x`.
     */
    def const[A](x: A): Iterator[A] = Iterator.continually(x)

    /**
     * Creates an infinite-length iterator of the current times.
     */
    def currentDate: Iterator[java.util.Date] = Iterator.continually(new java.util.Date)

    /**
     * Repeats an `Iterator` indefinitely.
     */
    def cycle[A](it: Iter[A]): Iterator[A] = Iterator.continually(()).flatMap(_ => it.ator)

    /**
     * Repeats an `Iterator` `n` times.
     */
    def repeat[A](it: Iter[A], n: Int): Iterator[A] = Iterator.continually(()).take(n).flatMap(_ => it.ator)

    /**
     * Repeats an `Iterator` while `p` returns `true`.
     */
    def repeatWhile[A](it: Iter[A])(p: => Boolean): Iterator[A] = Iterator.continually(()).takeWhile(_ => p).flatMap(_ => it.ator)

    /**
     * An infinite sequence of `next` time spans. (cf. JSR-310)
     */
    def timeSpan: Iterator[Long] = {
        var past = System.currentTimeMillis
        Iterator.continually {
            val cur  = System.currentTimeMillis
            val that = cur - past
            past = cur
            that
        }
    }

    /**
     * The dual of foldRight
     */
    def unfold[A, B](z: A)(op: A => Option[(B, A)]): Iterator[B] = new Unfold(z, op).concrete


    private class Unfold[A, +B](_1: A, _2: A => Option[(B, A)]) extends detail.AbstractIterator[B] {
        private[this] var _acc = _2(_1)
        override def isEnd = _acc.isEmpty
        override def deref = _acc.get._1
        override def increment() = _acc = _2(_acc.get._2)
    }
}
