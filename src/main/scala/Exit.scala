

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Exit function
 */
trait Exit {
    @annotation.threadSafe
    protected def rawApply(q: Exit.Status)

    @annotation.threadSafe @annotation.idempotent
    final def apply(q: Exit.Status = Exit.Success) = _apply(q)

    final def second: Exit = new Exit.Second(this)

    private[this] val _apply = detail.IfFirst[Exit.Status] { q => rawApply(q) } Else { _ => () }
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
    object Empty extends Exit {
        override protected def rawApply(q: Exit.Status) = ()
    }

    /**
     * Specifies a failure that reaction is exited by downstream.
     */
    case class ByOther(status: Status) extends RuntimeException


    private class Apply(_1: Exit.Status => Unit) extends Exit {
        override protected def rawApply(q: Exit.Status) = _1(q)
    }

    private class Second(_1: Exit) extends Exit {
        private[this] val _c = detail.IfFirst[Exit.Status] { _ => () } Else { q => _1(q) }
        override protected def rawApply(q: Exit.Status) = _c(q)
    }

    private[hano]
    class Two(_1: Reaction[_]) extends (Exit => Unit) {
        private[this] var en1, en2: Exit = Empty

        override def apply(p: Exit) {
            if (_1.isExited) {
                p()
            } else if (_1.isEntered) {
                en2 = p
            } else {
                en1 = p
                _1.enter {
                    Exit { _ =>
                        en1()
                        en2()
                    }
                }
            }
        }
    }
}
