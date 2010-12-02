

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private[hano] trait Conversions { self: Seq.type =>

    @Annotation.returnThat
    def from[A](that: Seq[A]): Seq[A] = that

    implicit def fromArray[A](from: Array[A]): Seq[A] = new FromArray(from)
    implicit def fromTraversable[A](from: scala.collection.Traversable[A]): Seq[A] = new FromTraversable(from)
    implicit def fromOption[A](from: Option[A]): Seq[A] = new FromOption(from)
    implicit def fromResponder[A](from: Responder[A]): Seq[A] = new FromResponder(from)
    implicit def fromReactor(from: Reactor): Seq[Any] = new Reactor.Secondary(from)
    /*implicit*/ def fromCps[A](from: => A @scala.util.continuations.suspendable): Seq[A] = new FromCps(from)

}
