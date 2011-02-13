

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Reaction {

    def apply[A](f: A => Unit, k: Exit => Unit): Reaction[A] = new Apply(f, k)

    @annotation.returnThat
    def from[A](that: Reaction[A]): Reaction[A] = that

    implicit def fromFunction[A](from: A => Unit): Reaction[A] = new FromFunction(from)
    implicit def fromValOption[A](from: Val[Option[A]]): Reaction[A] = from.toReaction

    private class Apply[A](_1: A => Unit, _2: Exit => Unit) extends Reaction[A] {
        override protected def rawApply(x: A) = _1(x)
        override protected def rawExit(q: Exit) = _2(q)
    }

    private class FromFunction[A](_1: A => Unit) extends Reaction[A] {
        override protected def rawApply(x: A) = _1(x)
        override protected def rawExit(q: Exit) = ()
    }
}


/**
 * Triggered by Seq.forloop
 */
trait Reaction[-A] {

    @annotation.returnThis @inline
    final def of[B <: A]: Reaction[B] = this

    @annotation.returnThis @inline
    final def asReaction: Reaction[A] = this

    /**
     * Has this reaction been exited?
     */
    final def isExited: Boolean = exited

    /**
     * Reacts on each element.
     */
    final def apply(x: A): Unit = mdf {
        if (!exited) {
            rawApply(x)
        }
    }

    /**
     * Reacts on the exit. (should not throw.)
     */
    final def exit(q: Exit): Unit = mdf {
        try {
            if (!exited) {
                exited = true
                rawExit(q)
            }
        } catch {
            case t: scala.util.control.ControlThrowable => throw t
            case t: Throwable => detail.LogErr(t, "Reaction.exit error")
        }
    }

    /**
     * Override to implement `apply`.
     */
    protected def rawApply(x: A): Unit

    /**
     * Override to implement `exit`.
     */
    protected def rawExit(q: Exit): Unit

    @annotation.equivalentTo("if (!isExited) body")
    final def beforeExit(body: => Unit) {
        if (!exited) {
            body
        }
    }

    @annotation.equivalentTo("exit(Exit.End)")
    final def end(): Unit = exit(Exit.End)

    @annotation.equivalentTo("exit(Exit.Closed)")
    final def closed(): Unit = exit(Exit.Closed)

    @annotation.equivalentTo("exit(Exit.Failed(why))")
    final def failed(why: Throwable): Unit = exit(Exit.Failed(why))

    private[this] var exited = false
    private[this] lazy val mdf = new detail.Modification(toString)

    private[hano]
    final def tryRethrow(body: => Unit) {
        try {
            body
        } catch {
            case t: Throwable => {
                exit(Exit.Failed(t)) // informs Reaction-site
                throw t // handled in Seq-site
            }
        }
    }
}
