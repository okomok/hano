

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Mixin for a sequence which doesn't allow re-foreach.
 */
trait ForloopOnce[A] extends Seq[A] {
    protected def forloopOnce(f: Reaction[A]): Unit

    private[this] val _forloop =
        detail.IfFirst[Reaction[A]] { f =>
            forloopOnce(f)
        } Else { _ =>
            throw new ForloopOnceException(this)
        }

    final override def forloop(f: Reaction[A]) = _forloop(f)
}

class ForloopOnceException[A](from: Seq[A]) extends
    RuntimeException("multiple `foreach` calls not allowed")
