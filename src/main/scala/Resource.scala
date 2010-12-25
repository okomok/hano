

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait Resource[A] extends Seq[A] with AsResource[A]

trait NoExitResource[A] extends Seq[A] with AsResource[A] with AsResource.NoExit[A]


/**
 * Mixin for a Seq resource.
 */
trait AsResource[A] extends ForloopOnce[A] { self: Seq[A] =>
    protected def openResource(f: Reaction[A]): Unit
    protected def closeResource(): Unit

    final override protected def forloopOnce(f: Reaction[A]) = openResource(f)
    final override def close() = c
    private[this] lazy val c = closeResource()
}

object AsResource {
    /**
     * Mixin for a Seq resource which has no end.
     */
    trait NoExit[A] { self: AsResource[A] =>
        protected def openResource(f: A => Unit): Unit
        final override protected def openResource(f: Reaction[A]): Unit = openResource(f(_))
    }
}

