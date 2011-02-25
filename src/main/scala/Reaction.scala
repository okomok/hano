

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Reaction {

    def apply[A](f: A => Unit, k: Exit => Unit): Reaction[A] = new Apply(f, k)

    @annotation.returnThat
    def from[A](that: Reaction[A]): Reaction[A] = that

    implicit def fromFunction[A](from: A => Unit): Reaction[A] = new FromFunction(from)
    implicit def fromVal[A](from: Val[A]): Reaction[A] = from.toReaction

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
trait Reaction[-A] extends java.io.Closeable {

    @annotation.returnThis @inline
    final def of[B <: A]: Reaction[B] = this

    @annotation.returnThis @inline
    final def asReaction: Reaction[A] = this

    /**
     * Has this reaction been entered?
     */
    final def isEntered: Boolean = _enter.isDone

    /**
     * Has this reaction been exited?
     */
    final def isExited: Boolean = _exit.isDone

    /**
     * Informs the entrance.
     */
    final def enter(c: => Unit) = _mdf { _enter {
        _close = c
    } }

    /**
     * Reacts on each element.
     */
    final def apply(x: A) = _mdf {
        if (_enter.isDone && !_exit.isDone) {
            try {
                rawApply(x)
            } catch {
                case detail.CloseException => exit(Exit.Closed)
            }
        }
    }

    /**
     * Reacts on the exit. (should not throw.)
     */
    final def exit(q: Exit) = _mdf { _exit {
        try {
            if (_enter.isDone) {
                _close()
                _close = null
                rawExit(q)
            }
        } catch {
            case detail.CloseException => ()
            case t: scala.util.control.ControlThrowable => throw t
            case t: Throwable => detail.LogErr(t, "Reaction.exit error")
        }
    } }

    @annotation.equivalentTo("exit(Exit.End)")
    final def end() = exit(Exit.End)

    @annotation.equivalentTo("exit(Exit.Closed)")
    final override def close() = exit(Exit.Closed)

    @annotation.equivalentTo("exit(Exit.Failed(why))")
    final def failed(why: Throwable) = exit(Exit.Failed(why))

    /**
     * Override this to implement `apply`.
     */
    protected def rawApply(x: A)

    /**
     * Override this to implement `exit`.
     */
    protected def rawExit(q: Exit)


    @annotation.equivalentTo("if (!isExited) body")
    final def beforeExit(body: => Unit) {
        if (!_exit.isDone) {
            body
        }
    }

    private[this] val _mdf = new detail.Modification(toString)
    private[this] val _enter = new detail.Once
    private[this] val _exit = new detailOnce
    private[this] var _close: () => Unit = null

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
