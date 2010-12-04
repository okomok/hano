

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


object Cursor {

    @hano.Annotation.returnThat
    def from[A](that: Cursor[A]): Cursor[A] = that

    implicit def fromJIterator[A](from: java.util.Iterator[A]): Cursor[A] = new FromJIterator(from)
    implicit def fromSIterator[A](from: scala.collection.Iterator[A]): Cursor[A] = new FromSIterator(from)

    private class FromJIterator[A](_1: java.util.Iterator[A]) extends Cursor[A] {
        private[this] var e = ready

        override def isEnd = e.isEmpty
        override def deref = e.get
        override def increment() = e = ready

        private def ready: Option[A] = if (_1.hasNext) Some(_1.next) else None
    }

    private class ToJIterator[A](_1: Cursor[A]) extends java.util.Iterator[A] {
        override def hasNext = !_1.isEnd
        override def next = {
            val tmp = _1.deref
            _1.increment
            tmp
        }
        override def remove = throw new UnsupportedOperationException("ToJIterator.remove")
    }

    private class FromSIterator[A](_1: scala.collection.Iterator[A]) extends Cursor[A] {
        private[this] var e = ready

        override def isEnd = e.isEmpty
        override def deref = e.get
        override def increment() = e = ready

        private def ready: Option[A] = if (_1.hasNext) Some(_1.next) else None
    }

    private class ToSIterator[A](_1: Cursor[A]) extends scala.collection.Iterator[A] {
        override def hasNext = !_1.isEnd
        override def next = {
            val tmp = _1.deref
            _1.increment
            tmp
        }
    }

}


/**
 * Yet another Iterator.
 * Unlike <code>Iterator</code>, this separates element-access and traversing method.
 */
trait Cursor[+A] {

    /**
     * Is cursor pass-the-end?
     */
    def isEnd: Boolean

    /**
     * Returns the current element.
     */
    def deref: A

    /**
     * Traverses to the next position.
     */
    def increment(): Unit

    @hano.Annotation.conversion
    final def toJIterator[B](implicit pre: Cursor[A] <:< Cursor[B]): java.util.Iterator[B] = new Cursor.ToJIterator(pre(this))

    @hano.Annotation.conversion
    final def toSIterator: scala.collection.Iterator[A] = new Cursor.ToSIterator(this)

    /**
     * Advances <code>n</code>.
     */
    final def advance(n: Int) {
        var it = n
        while (it != 0 && !isEnd) {
            increment()
            it -= 1
        }
    }

    /**
     * Advances while satisfying the predicate.
     */
    final def advanceWhile(p: A => Boolean) {
        while (!isEnd && p(deref)) {
            increment()
        }
    }
}
