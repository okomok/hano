

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Triggered by Seq.forloop
 */
trait Reaction[-A] {
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
    @annotation.idempotent
    final def enter(p: Exit = Exit.Empty): this.type = _mdf[this.type] {
        try {
            if (isExited) {
                p(_exitStatus)
            } else {
                _exitFuncs.offer(p)
            }

            _enter {
                rawEnter(_exitFuncs)
            }
        } catch {
            case t: Throwable => {
                _exitFuncs(Exit.Failure(t)) // clean up
                throw t // handled in Seq-site
            }
        }

        this
    }

    /**
     * Reacts on each element.
     */
    final def apply(x: A) = _mdf {
        require(isEntered, "`enter` shall be called before `apply` to: " + toString)

        if (!isExited) {
            rawApply(x)
        }
    }

    /**
     * Reacts on the exit. (should not throw.)
     */
    @annotation.idempotent
    final def exit(q: Exit.Status = Exit.Success) = _mdf {
        require(isEntered, "`enter` shall be called before `exit` to: " + toString)

        _exit {
            _exitStatus = q
            try {
                _exitFuncs(q)
                rawExit(q)
            } catch {
                case break.Control => ()
                case t: scala.util.control.ControlThrowable => throw t
                case t: Throwable => detail.LogErr(t, "aReaction.exit", true)
            }
        }
    }

    @annotation.equivalentTo("if (!isExited) body")
    final def beforeExit(body: => Unit) = if (!_exit.isDone) body

    /**
     * Override this to implement `enter`.
     */
    protected def rawEnter(p: Exit)

    /**
     * Override this to implement `apply`.
     */
    protected def rawApply(x: A)

    /**
     * Override this to implement `exit`.
     */
    protected def rawExit(q: Exit.Status)

    private[hano]
    final def applying(body: => Unit): this.type = {
        try {
            body
            this
        } catch {
            case t @ break.Control => {
                exit(Exit.Failure(t))
                this
            }
            case t: Throwable => {
                exit(Exit.Failure(t)) // informs Reaction-site
                throw t // handled in Seq-site
            }
        }
    }

    private[hano]
    final def applyingIn(pro: Process)(body: => Unit): this.type = {
        pro.head.onEnter {
            enter(_)
        } onEach { _ =>
            body
        } onExit {
            exit(_)
        } start()

        this
    }

    private[this] val _mdf = new detail.Modification(toString)
    private[this] var _enter = new detail.DoOnce
    private[this] val _exit = new detail.DoOnce
    private[this] var _exitFuncs = new Exit.Queue
    private[this] var _exitStatus: Exit.Status = null
}


object Reaction {
    def apply[A](j: Exit => Unit, f: A => Unit, k: Exit.Status => Unit): Reaction[A] = new Apply(j, f, k)

    @annotation.returnThat
    def from[A](that: Reaction[A]): Reaction[A] = that

    implicit def fromFunction[A](from: A => Unit): Reaction[A] = new FromFunction(from)
    implicit def fromVal[A](from: Val[A]): Reaction[A] = from.toReaction

    private class Apply[A](_1: Exit => Unit, _2: A => Unit, _3: Exit.Status => Unit) extends Reaction[A] {
        override protected def rawEnter(p: Exit) = _1(p)
        override protected def rawApply(x: A) = _2(x)
        override protected def rawExit(q: Exit.Status) = _3(q)
    }

    private class FromFunction[A](_1: A => Unit) extends Reaction[A] {
        override protected def rawEnter(p: Exit) = ()
        override protected def rawApply(x: A) = _1(x)
        override protected def rawExit(q: Exit.Status) = ()
    }
}
