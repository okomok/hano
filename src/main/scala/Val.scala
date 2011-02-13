

// Copyright Shunsuke Sogame 2010.
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
final class Val[A](override val context: Context = async) extends Seq[A] {

    require(context ne Self)
    require(context ne Unknown)

    private[this] val v = new concurrent.atomic.AtomicReference[Either[Throwable, A]](null)
    private[this] val fs = new concurrent.ConcurrentLinkedQueue[Reaction[A]]

    // subscription order is NOT preserved.
    override def forloop(f: Reaction[A]) {
        if (v.get != null) {
            _eval(f, v.get)
        } else {
            fs.offer(f)
            if (v.get != null && fs.remove(f)) {
                _eval(f, v.get)
            }
        }
    }

    private def _set(tx: Either[Throwable, A]) {
        if (v.compareAndSet(null, tx)) {
            while (!fs.isEmpty) {
                val f = fs.poll
                if (f != null) {
                    _eval(f, tx)
                }
            }
        } else {
            _check(v.get, tx)
        }
    }

    def set(x: A): Unit = _set(Right(x))

    def get: A = toFuture.apply()

    def fail(why: Throwable): Unit = _set(Left(why))

    def assign[B <: A](that: Seq[B]) = that.forloop(toReaction)

    @annotation.aliasOf("set")
    def update(x: A): Unit = set(x)

    @annotation.aliasOf("get")
    def apply(): A = get

    @annotation.aliasOf("assign")
    def :=[B <: A](that: Seq[B]): Unit = assign(that)

    def onAssign(f: A => Unit): Seq[A] = new Val.OnAssign(this, f)

    def toFuture: () => A = new Val.ToFuture(this)

    def toReaction: Reaction[A] = new Val.ToReaction(this)

    private def _eval(f: Reaction[A], tx: Either[Throwable, A]) {
        context onEach { _ =>
            tx match {
                case Left(t) => f.exit(Exit.Failed(t))
                case Right(x) => f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }

    private def _check(old: Either[Throwable, A], New: Either[Throwable, A]) {
        (old, New) match {
            case (Right(x), Right(y)) if x != y => {
                throw new Val.MultipleAssignmentException(old, New)
            }
            case _ => ()
        }
    }
}


object Val {

    /**
     * Thrown in case multiple assignment
     */
    class MultipleAssignmentException[A](expected: A, actual: A) extends
        RuntimeException("expected: " + expected + ", but actual: " + actual)

    /**
     * Returns a Val with initial value.
     */
    def apply[A](x: A): Val[A] = {
        val v = new Val[A]
        v() = x
        v
    }

    @annotation.equivalentTo("new Val[A]")
    def apply[A]: Val[A] = new Val[A]

    // REMOVE ME.
    def length(xs: Seq[_]): Val[Option[Int]] = {
        val v = new Val[Option[Int]](xs.context upper async)
        var acc = 0
        xs onEach { x =>
            acc += 1
        } onExit {
            case Exit.End => v() = Some(acc)
            case q => v() = None
        } start()
        v
    }

    // REMOVE ME.
    private class OnAssign[A](_1: Seq[A], _2: A => Unit) extends SeqProxy[A] {
        override val self = _1.onHead {
            case Some(x) => _2(x)
            case None => ()
        }
    }

    private class ToReaction[A](_1: Val[A]) extends Reaction[A] {
        override protected def rawApply(x: A) = _1.set(x)
        override protected def rawExit(q: Exit) = q match {
            case Exit.Failed(t) => _1.fail(t)
            case Exit.Closed => _1.fail(new NoSuchElementException("source sequence was closed before Val.set"))
            case _ => ()
        }
    }

    private class ToFuture[A](_1: Seq[A]) extends (() => A) {
        private[this] var v: Either[Throwable, A] = null
        private[this] val c = new java.util.concurrent.CountDownLatch(1)

        _1 onEach { x =>
            CountDown(c) {
                v = Right(x)
            }
        } onExit { q =>
            CountDown(c) {
                q match {
                    case Exit.Failed(t) if v == null => v = Left(t)
                    case _ => ()
                }
            }
        } start()

        override def apply(): A = {
            c.await()
            if (v == null) {
                throw new NoSuchElementException("Val.toFuture.apply()")
            }
            v match {
                case Left(t) => throw t
                case Right(r) => r
            }
        }
    }
}
