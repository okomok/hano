

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: ScalaFlow
//   at http://github.com/hotzen/ScalaFlow/raw/master/thesis.pdf


import java.util.concurrent
import detail.CountDown


/**
 * Single-assignment value as single-element sequence
 */
final class Val[A](override val process: Process = async) extends Seq[A] with detail.SingleSeq[A] {
    require(process ne Self)
    require(process ne Unknown)

    private[this] val v = new concurrent.atomic.AtomicReference[Either[Throwable, A]](null)
    private[this] val fs = new concurrent.ConcurrentLinkedQueue[Reaction[A]]

    /**
     * Will call a reaction when the value is initialized.
     * Subscription order is NOT preserved.
     */
    override def forloop(f: Reaction[A]) = _onSet(f)

    /**
     * Sets the value.
     */
    def set(x: A): Boolean = _set(Right(x))

    /**
     * Informs a failure to a reaction.
     */
    def setFailed(why: Throwable): Boolean = _set(Left(why))

    /**
     * Gets the value. (blocking)
     */
    def get(t: Within = Within.Inf): A = {
        var that: Option[A] = None

        onEach { x =>
            that = Some(x)
        } await()//(t)

        that match {
            case Some(x) => x
            case None => throw new NoSuchElementException("Val.get")
        }
    }

    /**
     * `Val` assignment
     */
    def assign(that: Seq[A]) = that.forloop(toReaction)

    /**
     * Equivalent to `set(x)`, but throws if the value is different.
     */
    def update(x: A) {
        if (!set(x)) {
            v.get match {
                case Right(y) if x != y => {
                    throw new Val.MultipleAssignmentException(y, x)
                }
                case _ => ()
            }
        }
    }

    @annotation.aliasOf("get")
    def apply(t: Within = Within.Inf): A = get(t)

    @annotation.aliasOf("assign")
    def :=[B <: A](that: Seq[B]) = assign(that)

    @annotation.conversion
    def toReaction: Reaction[A] = new Val.ToReaction(this)

    /**
     * Gets the value until the future.
     */
    def future: () => A = new detail.HeadFuture(this)

    private def _onSet(f: Reaction[A]) {
        if (v.get != null) {
            _eval(f, v.get)
        } else {
            fs.offer(f)
            if (v.get != null && fs.remove(f)) {
                _eval(f, v.get)
            }
        }
    }

    private def _set(tx: Either[Throwable, A]): Boolean = {
        if (v.compareAndSet(null, tx)) {
            while (!fs.isEmpty) {
                val f = fs.poll
                if (f != null) {
                    _eval(f, tx)
                }
            }
            true
        } else {
            false
        }
    }

    private def _eval(f: Reaction[A], tx: Either[Throwable, A]) {
        f.applyingIn(process) {
            tx match {
                case Left(t) => f.exit(Exit.Failure(t))
                case Right(x) => f(x)
            }
        }
    }
}


object Val {
    /**
     * Thrown in case multiple assignment
     */
    case class MultipleAssignmentException(expected: Any, actual: Any) extends
        RuntimeException("expected: " + expected + ", but actual: " + actual)

    /**
     * Creates a `Val` with initial value.
     */
    def apply[A](x: Seq[A]): Val[A] = {
        val v = new Val[A]
        v := x
        v
    }

    private class ToReaction[A](_1: Val[A]) extends Reaction[A] {
        override protected def rawEnter(p: Exit) = ()
        override protected def rawApply(x: A) = _1.set(x)
        override protected def rawExit(q: Exit.Status) = q match {
            case Exit.Failure(t) => _1.setFailed(t)
            case Exit.Success => _1.setFailed(new NoSuchElementException("sequence end before Val.set"))
        }
    }
}
