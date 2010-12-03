

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private[hano] trait Conversions { self: Seq.type =>

    @Annotation.returnThat
    def from[A](that: Seq[A]): Seq[A] = that

    implicit def fromArray[A](from: Array[A]): Seq[A] = new FromArray(from)
    implicit def fromJIterable[A](from: java.lang.Iterable[A]): Seq[A] = new FromTraversableOnce(util.Iter.able(from.iterator))
    implicit def fromTraversableOnce[A](from: scala.collection.TraversableOnce[A]): Seq[A] = new FromTraversableOnce(from)
    implicit def fromOption[A](from: Option[A]): Seq[A] = new FromOption(from)
    implicit def fromResponder[A](from: Responder[A]): Seq[A] = new FromResponder(from)
    implicit def fromReactor(from: Reactor): Seq[Any] = new Reactor.Secondary(from)
    /*implicit*/ def fromCps[A](from: => A @scala.util.continuations.suspendable): Seq[A] = new FromCps(from)

}


private class FromArray[A](_1: Array[A]) extends Forwarder[A] {
    override protected val delegate = Seq.from(_1)
}


private class FromTraversableOnce[A](_1: scala.collection.TraversableOnce[A]) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            _1.foreach(f)
        }
        k(End)
    }
}


private class FromOption[A](_1: Option[A]) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            if (!_1.isEmpty) {
                f(_1.get)
            }
        }
        k(End)
    }
}


private class FromResponder[A](_1: Responder[A]) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            _1.respond(f)
        }
        k(End)
    }
}

private class ToResponder[A](_1: Seq[A]) extends Responder[A] {
    override def respond(f: A => Unit) = _1.foreach(f)
}
