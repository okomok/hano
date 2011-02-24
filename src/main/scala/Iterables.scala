

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Contains methods missing from the standard.
 */
object Iterables {

    /**
     * Creates an `Iterable` from an `Iterator`.
     */
    def bind[A](it: => Iterator[A]): Iterable[A] = new Bind(() => it)

    /**
     * The dual of foldRight
     */
    def unfold[A, B](z: A)(op: A => Option[(B, A)]): Iterable[B] = new Unfold(z, op)

    /**
     * Creates an infinite iterator that repeatedly applies a given function to the previous result.
     */
    def iterate[A](z: A)(op: A => A): Iterable[A] = bind(Iterator.iterate(z)(op)).view

    /**
     * Cycles an `Iterable` indefinitely.
     */
    def cycle[A](iter: Iter[A]): Iterable[A] = bind(Iterator.continually(()).flatMap(_ => iter.ator)).view


    private class Bind[A](_1: () => Iterator[A]) extends Iterable[A] {
        override def iterator = _1()
    }

    private class Unfold[A, +B](_1: A, _2: A => Option[(B, A)]) extends Iterable[B] {
        override def iterator = new UnfoldIterator(_1, _2).concrete
    }

    private class UnfoldIterator[A, +B](_1: A, _2: A => Option[(B, A)]) extends detail.AbstractIterator[B] {
        private[this] var _acc = _2(_1)
        override def isEnd = _acc.isEmpty
        override def deref = _acc.get._1
        override def increment() = _acc = _2(_acc.get._2)
    }
}
