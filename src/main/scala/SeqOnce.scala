

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Disallows multiple `forloop`s.
 */
trait SeqOnce[A] extends Seq[A] {
    protected def forloopOnce(f: Reaction[A]): Unit

    private[this] val _forloop = {
        detail.IfFirst[Reaction[A]] { f =>
            forloopOnce(f)
        } Else { _ =>
            throw SeqOnce.MultipleForloopException(this)
        }
    }

    final override def forloop(f: Reaction[A]) = _forloop(f)
}


object SeqOnce {
    case class MultipleForloopException[A](seq: Seq[A]) extends
        RuntimeException("multiple `forloop` calls not allowed")

    trait Mixin[A] extends SeqOnce[A] {
        abstract override def forloopOnce(f: Reaction[A]) = super.forloop(f)
    }
}
