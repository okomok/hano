

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Cursor {

    @Annotation.returnThat
    def from[A](that: Cursor[A]): Cursor[A] = that

    implicit def fromIterator[A](from: Iterator[A]): Cursor[A] = new FromIterator(from)
    implicit def fromJIterator[A](from: java.util.Iterator[A]): Cursor[A] = new FromJIterator(from)

    private class FromIterator[A](_1: Iterator[A]) extends Cursor[A] {
        private[this] var x: Option[A] = ready
        override def isEnd = x.isEmpty
        override def deref = x.get
        override def increment() = x = ready
        private def ready: Option[A] = if (_1.hasNext) Some(_1.next) else None
    }

    private class ToIterator[A](_1: Cursor[A]) extends Iterator[A] {
        private[this] var exn: Throwable = null
        override def hasNext = {
            delayThrow()
            !_1.isEnd
        }
        override def next = {
            delayThrow()
            val tmp = _1.deref
            try {
                _1.increment
            } catch {
                case t: Throwable => exn = t
            }
            tmp
        }
        private def delayThrow() {
            if (exn != null) {
                throw exn
            }
        }
    }

    private class FromJIterator[A](_1: java.util.Iterator[A]) extends Cursor[A] {
        private[this] var x: Option[A] = ready
        override def isEnd = x.isEmpty
        override def deref = x.get
        override def increment() = x = ready
        private def ready: Option[A] = if (_1.hasNext) Some(_1.next) else None
    }

    private class ToJIterator[A](_1: Cursor[A]) extends java.util.Iterator[A] {
        private[this] var exn: Throwable = null
        override def hasNext = {
            delayThrow()
            !_1.isEnd
        }
        override def next = {
            delayThrow()
            val tmp = _1.deref
            try {
                _1.increment
            } catch {
                case t: Throwable => exn = t
            }
            tmp
        }
        private def delayThrow() {
            if (exn != null) {
                throw exn
            }
        }
        override def remove = throw new UnsupportedOperationException("ToJIterator.remove")
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

    @Annotation.conversion
    final def toIterator: Iterator[A] = new Cursor.ToIterator(this)

    @Annotation.conversion
    final def toJIterator[B](implicit pre: Cursor[A] <:< Cursor[B]): java.util.Iterator[B] = new Cursor.ToJIterator(pre(this))

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
