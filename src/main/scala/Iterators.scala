

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Contains methods missing from the standard.
 */
object Iterators {

    /**
     * Binds an `Iterator` to `Iterable`.
     */
    def bind[A](it: => Iterator[A]): Iterable[A] = new Bind(() => it)

    /**
     * Creates an infinite-length iterator returning `x`.
     */
    def const[A](x: A): Iterator[A] = new Const(x)

    /**
     * Creates an infinite-length iterator of the current times.
     */
    def currentDate: Iterator[java.util.Date] = Iterator.continually(new java.util.Date)

    /**
     * Repeats an `Iterator` indefinitely.
     */
    def cycle[A](iter: Iter[A]): Iterator[A] = const(()).flatMap(_ => iter.ator)

    /**
     * Repeats an `Iterator` `n` times.
     */
    def repeat[A](iter: Iter[A], n: Int): Iterator[A] = const(()).take(n).flatMap(_ => iter.ator)

    /**
     * Repeats an `Iterator` while `p` returns `true`.
     */
    def repeatWhile[A](iter: Iter[A])(p: => Boolean): Iterator[A] = const(()).takeWhile(_ => p).flatMap(_ => iter.ator)

    /**
     * An infinite sequence of `next` time spans. (cf. JSR-310)
     */
    def timeSpan: Iterator[Long] = {
        var past = System.currentTimeMillis
        Iterator.continually {
            val cur = System.currentTimeMillis
            val res = cur - past
            past = cur
            res
        }
    }

    /**
     * The dual of foldRight
     */
    def unfold[A, B](z: A)(op: A => Option[(B, A)]): Iterator[B] = new Unfold(z, op).concrete

    /**
     * Closes an attached resource if closeable.
     */
    def close(it: Iterator[_]) {
        it match {
            case it: java.io.Closeable => it.close()
            case _ => ()
        }
    }


    private class Bind[A](_1: () => Iterator[A]) extends Iterable[A] {
        override def iterator = _1()
    }

    private class Const[A](_1: A) extends Iterator[A] {
        override def hasNext = true
        override def next = _1
    }

    private class Unfold[A, +B](_1: A, _2: A => Option[(B, A)]) extends detail.AbstractIterator[B] {
        private[this] var _acc = _2(_1)
        override def isEnd = _acc.isEmpty
        override def deref = _acc.get._1
        override def increment() = _acc = _2(_acc.get._2)
    }
}
