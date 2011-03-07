

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Exit function
 */
trait Exit {
    @annotation.returnThis @inline
    final def asExit: Exit = this

    @annotation.threadSafe @annotation.idempotent
    def apply(q: Exit.Status = Exit.Success)

    @annotation.threadSafe
    def isDone: Boolean
}


object Exit {
    /**
     * Exit status
     */
    sealed abstract class Status extends Message {
        @annotation.returnThis @inline
        final def asStatus: Status = this
    }

    /**
     * Success
     */
    case object Success extends Status

    /**
     * Failure
     */
    case class Failure(why: Throwable) extends Status

    /**
     * Used to handle an exit by default.
     */
    object defaultHandler extends (Exit.Status => Unit) {
        override def apply(q: Exit.Status) = q match {
            case Exit.Failure(t) => detail.LogErr(t, "unhandled failure")
            case _ => ()
        }
    }

    /**
     * Creates an exit function from `k`.
     */
    @annotation.pre("`k` is thread-safe")
    def apply(k: Exit.Status => Unit): Exit = new Apply(k)

    /**
     * The exit function to do nothing
     */
    object Empty extends Idempotent {
        override protected def rawApply(q: Exit.Status) = ()
    }

    /**
     * Specifies a failure that reaction is exited by downstream.
     */
    case class ByOther(status: Status) extends RuntimeException

    /**
     * Mixin for a trivial implementation.
     */
    trait Idempotent extends Exit {
        @annotation.threadSafe
        protected def rawApply(q: Exit.Status)

        @annotation.threadSafe @annotation.idempotent
        final override def apply(q: Exit.Status = Exit.Success) = _apply(q)

        final override def isDone: Boolean = _apply.isSecond

        private[this] val _apply = detail.IfFirst[Exit.Status] { q => rawApply(q) } Else { _ => () }
    }


    private class Apply(_1: Exit.Status => Unit) extends Idempotent {
        override protected def rawApply(q: Exit.Status) = _1(q)
    }

    private[hano]
    class Second(_1: Exit) extends Idempotent {
        private[this] val _c = detail.IfFirst[Exit.Status] { _ => () } Else { q => _1(q) }
        override protected def rawApply(q: Exit.Status) = _c(q)
    }

    private[hano]
    class Queue extends Exit {
        private[this] val ps = new java.util.concurrent.ConcurrentLinkedQueue[Exit]

        override def apply(q: Exit.Status = Exit.Success) {
            var p = ps.poll()
            while (p ne null) {
                p(q)
                p = ps.poll()
            }
        }

        override def isDone: Boolean = ps.isEmpty

        def offer(q: Exit) {
            ps.offer(q)
        }
    }
}
