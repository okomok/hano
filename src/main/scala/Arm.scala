

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Arm {

    @Annotation.returnThat
    def from[A](that: Arm[A]): Arm[A] = that

    implicit def fromJCloseable[A <: java.io.Closeable](from: A): Arm[A] = new FromJCloseable(from)
    implicit def fromJLock[A <: java.util.concurrent.locks.Lock](from: A): Arm[A] = new FromJLock(from)

    private class FromJCloseable[A <: java.io.Closeable](_1: A) extends Arm[A] {
        override def open = _1
        override def close() = _1.close()
    }

    private class FromJLock[A <: java.util.concurrent.locks.Lock](_1: A) extends Arm[A] {
        override def open = { _1.lock; _1 }
        override def close() = _1.unlock
    }

    private class UsedBy[A, B](_1: Arm[A], _2: A => Seq[B]) extends Seq[B] {
        override def close() = _1.close()

        @Annotation.pre("f is synchronous")
        override def forloop(f: B => Unit, k: Exit => Unit) {
            val r = _1.open
            _2(r) _for { y =>
                for (_ <- from(_1: java.io.Closeable)) {
                    f(y)
                }
            } _andThen { q =>
                k(q)
                close()
            }
        }
    }
}


/**
 * Mixin for automatic resource management
 */
trait Arm[+A] extends Seq[A] {
    def open: A

    @Annotation.pre("f is synchronous")
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val r = open
        var primary: Throwable = null
        try {
            f(r)
        } catch {
            case t: Throwable => {
                primary = t
                k(Exit.Thrown(t))
                throw t
            }
        } finally {
            if (primary != null) {
                try {
                    close()
                } catch {
                    case s: Exception => /*primary.addSuppressedException(s)*/
                }
            } else {
                k(Exit.End)
                close()
            }
        }
    }

    def usedBy[B](f: A => Seq[B]): Seq[B] = new Arm.UsedBy(this, f)
}
