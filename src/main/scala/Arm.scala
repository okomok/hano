

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
        override def forloop(f: Reaction[B]) {
            val r = _1.open
            detail.For(_2(r)) { y =>
                for (_ <- from(_1: java.io.Closeable)) {
                    f(y)
                }
            } AndThen { q =>
                f.exit(q)
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
    override def forloop(f: Reaction[A]) {
        val r = open
        var primary: Throwable = null
        try {
            f(r)
        } catch {
            case t: Throwable => {
                primary = t
                f.exit(Exit.Failed(t))
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
                f.exit(Exit.End)
                close()
            }
        }
    }

    def usedBy[B](f: A => Seq[B]): Seq[B] = new Arm.UsedBy(this, f)
}
