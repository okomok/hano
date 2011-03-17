

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
}


object Exit {

    /**
     * Exit status
     */
    sealed abstract class Status extends Message {
        @annotation.returnThis @inline
        final def asStatus: Status = this
        final def isSuccess: Boolean = this == Success
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
     * Creates an exit function from `k`.
     */
    @annotation.pre("`k` is thread-safe")
    def apply(k: Status => Unit): Exit = new Apply(k)

    /**
     * The exit function to do nothing
     */
    object Empty extends Idempotent {
        override protected def rawApply(q: Status) = ()
    }

    /**
     * Specifies a failure that reaction is exited in downstream.
     */
    case class ByOther(status: Status) extends RuntimeException

    /**
     * Mixin for a trivial implementation.
     */
    trait Idempotent extends Exit {
        @annotation.threadSafe
        protected def rawApply(q: Status)

        @annotation.threadSafe @annotation.idempotent
        final override def apply(q: Status = Success) = _apply(q)

        private[this] val _apply = detail.IfFirst[Status] { q => rawApply(q) } Else { _ => () }
    }


    private class Apply(_1: Status => Unit) extends Idempotent {
        override protected def rawApply(q: Status) = _1(q)
    }

    private[hano]
    class Queue extends Exit {
        private[this] val ps = new java.util.ArrayDeque[Exit]
            //new java.util.concurrent.ConcurrentLinkedQueue[Exit]

        override def apply(q: Status = Success): Unit = synchronized {
            var p = ps.poll()
            while (p ne null) {
                p(q)
                p = ps.poll()
            }
        }

        def offer(p: Exit): Unit = synchronized { if (p ne Empty) ps.offer(p) }
        def remove(p: Exit): Unit = synchronized { ps.remove(p) }
    }
}
