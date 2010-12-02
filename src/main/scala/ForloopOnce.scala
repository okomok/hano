

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Mixin for a sequence which doesn't allow re-foreach.
 */
trait ForloopOnce[A] extends Seq[A] {
    protected def forloopOnce(f: A => Unit, k: Exit => Unit): Unit

    private[this] val _forloop =
        IfFirst[(A => Unit, Exit => Unit)] { case (f, k) =>
            forloopOnce(f, k)
        } Else { _ =>
            throw new ForloopOnceException(this)
        }

    final override def forloop(f: A => Unit, k: Exit => Unit) = _forloop((f, k))
}

class ForloopOnceException[A](from: Seq[A]) extends
    RuntimeException("multiple `foreach` calls not allowed")
