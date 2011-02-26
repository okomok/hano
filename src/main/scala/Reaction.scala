

// Copyright Shunsuke Sogame 2010.
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
    final def enter(p: Entrance) = _mdf {
        _enter {
            _entrance = p
            rawEnter(p)
        }
    }

    /**
     * Reacts on each element.
     */
    final def apply(x: A) = _mdf {
        require(_enter.isDone, "`enter` shall be called before `apply`")

        if (_enter.isDone && !_exit.isDone) {
            try {
                rawApply(x)
            } catch {
                case t @ detail.BreakControl => exit(Exit.Failed(t))
            }
        }
    }

    /**
     * Reacts on the exit. (should not throw.)
     */
    @annotation.idempotent
    final def exit(q: Exit) = _mdf {
        require(_enter.isDone, "`enter` shall be called before `exit`")

        _exit {
            try {
                _entrance.close()
                rawExit(q)
            } catch {
                case detail.BreakControl => ()
                case t: scala.util.control.ControlThrowable => throw t
                case t: Throwable => detail.LogErr(t, "aReaction.exit")
            }
        }
    }

    @annotation.equivalentTo("enter(new Entrance(() => ()))")
    final def enter(b: => Unit): Unit = enter(new Entrance(() => b))

    @annotation.equivalentTo("exit(Exit.End)")
    final def end() = exit(Exit.End)

    @annotation.equivalentTo("exit(Exit.Failed(why))")
    final def failed(why: Throwable) = exit(Exit.Failed(why))

    @annotation.equivalentTo("if (!isExited) body")
    final def beforeExit(body: => Unit) = if (!_exit.isDone) body

    /**
     * Override this to implement `enter`.
     */
    protected def rawEnter(p: Entrance)

    /**
     * Override this to implement `apply`.
     */
    protected def rawApply(x: A)

    /**
     * Override this to implement `exit`.
     */
    protected def rawExit(q: Exit)


    private[this] var _entrance: Entrance = null
    private[this] val _mdf = new detail.Modification(toString)
    private[this] val _enter = new detail.DoOnce
    private[this] val _exit = new detail.DoOnce

    private[hano]
    final def _do(body: => Unit) {
        try {
            body
        } catch {
            case t @ detail.BreakControl => failed(t)
            case t: Throwable => {
                failed(t) // informs Reaction-site
                throw t // handled in Seq-site
            }
        }
    }
}


object Reaction {

    def apply[A](j: Entrance => Unit, f: A => Unit, k: Exit => Unit): Reaction[A] = new Apply(j, f, k)

    @annotation.returnThat
    def from[A](that: Reaction[A]): Reaction[A] = that

    implicit def fromFunction[A](from: A => Unit): Reaction[A] = new FromFunction(from)
    implicit def fromVal[A](from: Val[A]): Reaction[A] = from.toReaction

    private class Apply[A](_1: Entrance => Unit, _2: A => Unit, _3: Exit => Unit) extends Reaction[A] {
        override protected def rawEnter(p: Entrance) = _1(p)
        override protected def rawApply(x: A) = _2(x)
        override protected def rawExit(q: Exit) = _3(q)
    }

    private class FromFunction[A](_1: A => Unit) extends Reaction[A] {
        override protected def rawEnter(p: Entrance) = ()
        override protected def rawApply(x: A) = _1(x)
        override protected def rawExit(q: Exit) = ()
    }
}
