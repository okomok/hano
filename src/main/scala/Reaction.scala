

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Triggered by Seq.forloop
 */
trait Reaction[-A] {

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


object Reaction {

    class MultipleExitsException[A](reaction: Reaction[A]) extends RuntimeException("multiple `exit` calls not allowed")
    class ApplyAfterExitException[A](reaction: Reaction[A]) extends RuntimeException("`apply` shall not be called after exit")

    def apply[A](f: A => Unit, k: Exit => Unit): Reaction[A] = new Reaction[A] with Checked[A] {
        override protected def applyChecked(x: A) = f(x)
        override protected def exitChecked(q: Exit) = k(q)
    }

    /**
     * Mixin to kick non-conforming Seq
     */
    trait Checked[-A] { self: Reaction[A] =>
        protected def applyChecked(x: A): Unit
        protected def exitChecked(q: Exit): Unit

        private val _k = detail.IfFirst[Exit] { q =>
            exitChecked(q)
        } Else { _ =>
            throw new MultipleExitsException(self)
        }

        final override def apply(x: A) {
            if (_k.isSecond) {
                throw new ApplyAfterExitException(self)
            } else {
                applyChecked(x)
            }
        }
        final override def exit(q: Exit) = _k(q)
    }


    @Annotation.returnThat
    def from[A](that: Reaction[A]): Reaction[A] = that

    implicit def fromFunction[A](f: A => Unit): Reaction[A] = new Reaction[A] {
        override def apply(x: A) = f(x)
        override def exit(q: Exit) = ()
    }
    implicit def fromVar[A](from: Var[A]): Reaction[A] = from.toReaction
    implicit def fromRist[A](from: Rist[A]): Reaction[A] = from.toReaction

}
