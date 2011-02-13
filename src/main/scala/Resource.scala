

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Helps to implement Seq as resource.
 */
@annotation.mixin
trait Resource[A] extends SeqOnce[A] {

    protected def openResource(f: Reaction[A]): Unit
    protected def closeResource(): Unit

    private[this] lazy val _close = closeResource()

    final override def close() = _close
    final override def forloopOnce(f: Reaction[A]) = openResource(f)
}


/**
 * Helps to implement infinite Seq as resource.
 */
@annotation.mixin
trait NoExitResource[A] extends Resource[A] {
    protected def openResource(f: A => Unit): Unit
    final override protected def openResource(f: Reaction[A]): Unit = openResource(f(_))
}
