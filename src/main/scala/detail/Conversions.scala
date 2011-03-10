

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.util.continuations


private[hano]
trait Conversions { self: Seq.type =>
    @annotation.returnThat
    def from[A](that: Seq[A]): Seq[A] = that

    implicit def fromArray[A](from: Array[A]): Seq[A] = new FromIter(from)
    implicit def fromIter[A](from: Iter[A]): Seq[A] = new FromIter(from)
    implicit def fromIterable[A](from: scala.collection.Iterable[A]): Seq[A] = new FromIter(from)
    implicit def fromTraversableOnce[A](from: scala.collection.TraversableOnce[A]): Seq[A] = new FromTraversableOnce(from)
    implicit def fromJIterable[A](from: java.lang.Iterable[A]): Seq[A] = new FromIter(from)
    implicit def fromOption[A](from: Option[A]): Seq[A] = new FromIter(from)
    implicit def fromResponder[A](from: Responder[A]): Seq[A] = new FromResponder(from)
    implicit def fromReactor(from: Reactor): Seq[Any] = new Reactor.Secondary(from)
    /*implicit not work*/ def fromCps[A](from: => A @continuations.suspendable): Seq[A] = new FromCps(from)
}


private[hano]
class FromTraversableOnce[A](_1: scala.collection.TraversableOnce[A]) extends Seq[A] {
    override def process = Self

    override def forloop(f: Reaction[A]) {
        f.applyingIn(process) {
            _1.foreach(f(_))
        }
    }
}

private[hano]
class ToTraversable[A](_1: Seq[A]) extends scala.collection.Traversable[A] {
    override def foreach[U](f: A => U) = _1.foreach(f(_))
}


private[hano]
class ToIterable[A](_1: Seq[A]) extends Iterable[A] {
    override def iterator = {
        if (_1.process eq Self) {
            new SyncIterable(_1).iterator
        } else {
            new AsyncIterable(_1).iterator
        }
    }
}


private[hano]
class FromResponder[A](_1: Responder[A]) extends Seq[A] {
    override def process = Self

    override def forloop(f: Reaction[A]) {
        f.applyingIn(process) {
            _1.respond(f(_))
        }
    }
}

private[hano]
class ToResponder[A](_1: Seq[A]) extends Responder[A] {
    override def respond(f: A => Unit) = _1.foreach(f)
}


private[hano]
class FromCps[A](from: => A @continuations.suspendable) extends Seq[A] {
    override def process = Self

    override def forloop(f: Reaction[A]) {
        f.applyingIn(process) {
            continuations.reset {
                f(from)
            }
        }
    }
}
