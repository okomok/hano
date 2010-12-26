

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Mixin for a sequence which doesn't allow re-forloop.
 */
trait ForloopOnce[A] { self: Seq[A] =>
    protected def forloopOnce(f: Reaction[A]): Unit

    private val _forloop =
        detail.IfFirst[Reaction[A]] { f =>
            forloopOnce(f)
        } Else { _ =>
            throw new ForloopOnceException(this)
        }

    final override def forloop(f: Reaction[A]) = _forloop(f)
}

class ForloopOnceException[A](seq: Seq[A]) extends
    RuntimeException("multiple `foreach` calls not allowed")
