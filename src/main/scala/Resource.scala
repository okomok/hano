

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Mixin for a Seq resource.
 */
trait Resource[A] extends ForloopOnce[A] {
    protected def openResource(f: A => Unit, k: Exit => Unit): Unit
    protected def closeResource(): Unit

    final override protected def forloopOnce(f: A => Unit, k: Exit => Unit) = openResource(f, k)
    final override def close() = c
    private[this] lazy val c = closeResource()
}

/**
 * Mixin for a Seq resource which has no end.
 */
trait NoExitResource[A] extends ForloopOnce[A] {
    protected def openResource(f: A => Unit): Unit
    protected def closeResource(): Unit

    final override protected def forloopOnce(f: A => Unit, k: Exit => Unit) = openResource(f)
    final override def close() = c
    private[this] lazy val c = closeResource()
}
