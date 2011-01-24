

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Reaction {

    def apply[A](f: A => Unit, k: Exit => Unit): Reaction[A] = new Apply(f, k)

    @Annotation.returnThat
    def from[A](that: Reaction[A]): Reaction[A] = that

    implicit def fromFunction[A](from: A => Unit): Reaction[A] = new FromFunction(from)

    private class Apply[A](_1: A => Unit, _2: Exit => Unit) extends CheckedReaction[A] {
        override protected def checkedApply(x: A) = _1(x)
        override protected def checkedExit(q: Exit) = _2(q)
    }

    private class FromFunction[A](_1: A => Unit) extends CheckedReaction[A] {
        override protected def checkedApply(x: A) = _1(x)
        override protected def checkedExit(q: Exit) = ()
    }
}


/**
 * Triggered by Seq.forloop
 */
trait Reaction[-A] {

    @Annotation.returnThis @inline
    final def of[B <: A]: Reaction[B] = this

    @Annotation.returnThis @inline
    final def asReaction: Reaction[A] = this

    /**
     * Reacts on each element.
     */
    def apply(x: A): Unit

    /**
     * Reacts on the exit. (should not throw.)
     */
    def exit(q: Exit): Unit

    @Annotation.equivalentTo("exit(Exit.End)")
    final def end(): Unit = exit(Exit.End)

    @Annotation.equivalentTo("exit(Exit.Closed)")
    final def closed(): Unit = exit(Exit.Closed)

    @Annotation.equivalentTo("exit(Exit.Failed(why))")
    final def failed(why: Throwable): Unit = exit(Exit.Failed(why))

    private[hano]
    final def tryRethrow(body: => Unit) {
        try {
            body
        } catch {
            case t: Throwable => {
                exitNothrow(Exit.Failed(t)) // informs Reaction-site
                throw t // handled in Seq-site
            }
        }
    }

    private[hano]
    final def exitNothrow(q: Exit) {
        try {
            exit(q)
        } catch {
            case t: Throwable => detail.LogErr(t, "Reaction.exit error")
        }
    }
}
