

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Reaction {

    def apply[A](f: A => Unit, k: Exit => Unit): Reaction[A] = new Apply(f, k)

    @Annotation.returnThat
    def from[A](that: Reaction[A]): Reaction[A] = that

    implicit def fromFunction[A](from: A => Unit): Reaction[A] = new FromFunction(from)
    implicit def fromVar[A](from: Var[A]): Reaction[A] = from.toReaction
    implicit def fromRist[A](from: Rist[A]): Reaction[A] = from.toReaction

    private class Apply[A](_1: A => Unit, _2: Exit => Unit) extends Reaction[A] with Checked[A] {
        override protected def applyChecked(x: A) = _1(x)
        override protected def exitChecked(q: Exit) = _2(q)
    }

    private class FromFunction[A](_1: A => Unit) extends Reaction[A] with Checked[A] {
        override protected def applyChecked(x: A) = _1(x)
        override protected def exitChecked(q: Exit) = ()
    }

    class NotSerializedException[A](reaction: Reaction[A]) extends RuntimeException("method calls shall be serialized")
    class MultipleExitsException[A](reaction: Reaction[A]) extends RuntimeException("multiple `exit` calls not allowed")
    class ApplyAfterExitException[A](reaction: Reaction[A]) extends RuntimeException("`apply` shall not be called after exit")

    /**
     * Mixin to kick non-conforming Seq
     */
    trait Checked[-A] { self: Reaction[A] =>
        protected def applyChecked(x: A): Unit
        protected def exitChecked(q: Exit): Unit

        @volatile private var _ing = false
        private val _exit = {
            detail.IfFirst[Exit] { q =>
                exitChecked(q)
            } Else { _ =>
                throw new MultipleExitsException(self)
            }
        }
        private def _apply(x: A) {
            if (_exit.isSecond) {
                throw new ApplyAfterExitException(self)
            } else {
                applyChecked(x)
            }
        }

        final override def apply(x: A) {
            if (_ing) {
                throw new NotSerializedException(self)
            }
            try {
                _ing = true
                _apply(x)
            } finally {
                _ing = false
            }
        }
        final override def exit(q: Exit) = {
            if (_ing) {
                throw new NotSerializedException(self)
            }
            _exit(q)
        }
    }

}


/**
 * Triggered by Seq.forloop
 */
trait Reaction[-A] { self =>

    /**
     * Reacts on each element.
     */
    def apply(x: A): Unit

    /**
     * Reacts on the exit.
     */
    def exit(q: Exit): Unit

    @Annotation.equivalentTo("exit(Exit.End)")
    final def end(): Unit = exit(Exit.End)

    @Annotation.equivalentTo("exit(Exit.Closed)")
    final def closed(): Unit = exit(Exit.Closed)

    @Annotation.equivalentTo("exit(Exit.Failed(why))")
    final def failed(why: Throwable): Unit = exit(Exit.Failed(why))

}
