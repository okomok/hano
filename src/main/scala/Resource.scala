

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import detail.IfFirst


/**
 * Helps to implement Seq as resource.
 */
@annotation.mixin
trait Resource[A] extends Seq[A] {
    protected def openResource(f: Reaction[A]): Unit
    protected def closeResource(): Unit

    private[this] lazy val _close = closeResource()
    private[this] val _forloop = {
        IfFirst[Reaction[A]] { f =>
            openResource(f)
        } Else { _ =>
            throw new ResourceException(this)
        }
    }

    final override def close() = _close
    final override def forloop(f: Reaction[A]) = _forloop(f)
}

/**
 * Helps to implement infinite Seq as resource.
 */
@annotation.mixin
trait NoExitResource[A] extends Resource[A] {
    protected def openResource(f: A => Unit): Unit
    final override protected def openResource(f: Reaction[A]): Unit = openResource(f(_))
}


class ResourceException[A](seq: Seq[A]) extends
    RuntimeException("multiple `forloop` calls not allowed")
