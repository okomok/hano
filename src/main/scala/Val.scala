

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: ScalaFlow
//   at http://github.com/hotzen/ScalaFlow/raw/master/thesis.pdf


import java.util.concurrent


/**
 * Single-assignment value as single-element sequence
 */
final class Val[A](override val process: Process = async) extends Seq[A] with detail.SingleSeq[A] {
    Require.notSelf(process, "Val")
    Require.notUnknown(process, "Val")

    private[this] val _v = new concurrent.atomic.AtomicReference[Either[Throwable, A]](null)
    private[this] val _fs = new concurrent.ConcurrentLinkedQueue[Reaction[A]]

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
    def get(_timeout: Long = NO_TIMEOUT): A = {
        var that: Option[A] = None

        head.onEach { x =>
            that = Some(x)
        } await(_timeout)

        that match {
            case Some(x) => x
            case None => throw new NoSuchElementException("aVal.get")
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
            _v.get match {
                case Right(y) if x != y => {
                    throw new Val.MultipleAssignmentException(y, x)
                }
                case _ => ()
            }
        }
    }

    @annotation.aliasOf("get")
    def apply(_timeout: Long = NO_TIMEOUT): A = get(_timeout)

    @annotation.aliasOf("assign")
    def :=(that: Seq[A]) = assign(that)

    @annotation.conversion
    def toReaction: Reaction[A] = new Val.ToReaction(this)

    private def _onSet(f: Reaction[A]) {
        if (_v.get != null) {
            _eval(f, _v.get)
        } else {
            _fs.offer(f)
            if (_v.get != null && _fs.remove(f)) {
                _eval(f, _v.get)
            }
        }
    }

    private def _set(tx: Either[Throwable, A]): Boolean = {
        if (_v.compareAndSet(null, tx)) {
            detail.Polleach(_fs) { f =>
                _eval(f, tx)
            }
            true
        } else {
            false
        }
    }

    private def _eval(f: Reaction[A], tx: Either[Throwable, A]) {
        f.applyingIn(process) {
            tx match {
                case Left(t) => f.fail(t)
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
        IllegalStateException("expected: " + expected + ", but actual: " + actual)

    /**
     * Creates a `Val` with initial value.
     */
    def apply[A](that: Seq[A]): Val[A] = {
        val v = new Val[A]
        v := that
        v
    }

    private class ToReaction[A](_1: Val[A]) extends Reaction[A] {
        override protected def rawApply(x: A) {
            _1.set(x)
            exit()
        }
        override protected def rawExit(q: Exit.Status) = q match {
            case Exit.Failure(t) => _1.setFailed(t)
            case Exit.Success => _1.setFailed(new NoSuchElementException("sequence end before aVal.set"))
        }
    }
}
