

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Contains some utilities.
 */
object Util {

    /**
     * Builds a collection from an `Iterable`.
     */
    def build[A, To](iter: Iter[A], _b: => scala.collection.mutable.Builder[A, To]): To = {
        // Builder is taken by-name, for it can't be shared.(e.g. mutable.ArrayBuffer)
        val b = _b
        for (x <- iter.able) {
            b += x
        }
        b.result
    }

    /**
     * Builds a collection from an `Iterable` using a target type implicitly.
     */
    def buildFrom[A, To](iter: Iter[A])(implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): To = {
        build(iter, bf())
    }

    /**
     * Counts down a latch.
     */
    def countDown[A](c: java.util.concurrent.CountDownLatch)(body: => A): A = {
        try {
            body
        } finally {
            c.countDown()
        }
    }

    /**
     * Locks and unlocks a lock.
     */
    def syncBy[A](l: java.util.concurrent.locks.Lock)(body: => A): A = {
        l.lock()
        try {
            body
        } finally {
            l.unlock()
        }
    }

    @annotation.equivalentTo("assert(assertion)")
    def verify[A](assertion: Boolean): Unit = assert(assertion)

    @annotation.equivalentTo("assert(assertion, message)")
    def verify[A](assertion: Boolean, message: String): Unit = assert(assertion, message)

    /**
     * Turns `f` into a function which ignores the second call.
     */
    def once[A](f: A => Unit): A => Unit = detail.IfFirst[A] { f(_) } Else { _ => () } toFunction

    /**
     * Default value, used with implicit.
     */
    final class Default[A](val value: A) extends (() => A) {
        override def apply(): A = value
    }

    object Default {
        implicit def fromAny[A](from: A): Default[A] = new Default(from)

        final class ByName[A](val value: () => A) extends (() => () => A) {
            override def apply(): () => A = value
        }
        object ByName {
            implicit def fromByName[A](from: => A): ByName[A] = new ByName(() => from)
        }
    }

    /**
     * Evaluates an expression only once.
     */
    @annotation.notThreadSafe
    final class DoOnce {
        // @volatile is nothing but a hint.
        @volatile private[this] var _done = false

        def isDone: Boolean = _done

        def apply(body: => Unit) {
            if (!_done) {
                _done = true
                body
            }
        }
    }

    /**
     * Helps to implement open-close resources.
     */
    final class Live(_die: => Unit, _err: => Throwable) {
        @annotation.compilerWorkaround("2.8.0")
        private[this] lazy val _doDie: Int = { _die; 0 }

        @volatile private[this] var _dead = false

        def die() {
            _dead = true
            _doDie
        }

        def apply[A](body: => A): A = {
            if (_dead) {
                throw _err
            } else {
                body
            }
        }
    }

    /**
     * Offers fail-fast behavior based on a best-effort basis.
     */
    final class Modify(msg: => String) {
        @volatile private[this] var _ing: Thread = null

        def apply[A](body: => A): A = {
            val cur = Thread.currentThread()

            if ((_ing ne null) && (_ing ne cur)) {
                throw new java.util.ConcurrentModificationException(msg)
            }

            try {
                _ing = cur
                body
            } finally {
                _ing = null
            }
        }
    }
}
