

// Copyright Shunsuke Sogame 2010.
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
class FromIter[A](_1: Iter[A]) extends Seq[A] {
    override def context = Self

    override def forloop(f: Reaction[A]) {
        var isActive = true
        f.enter {
            isActive = false
        }
        f._do {
            val it = _1.ator
            while (isActive && it.hasNext) {
                f(it.next)
            }
        }
        if (isActive) {
            f.exit(Exit.End)
        } else {
            f.exit(Exit.Failed(break.Control))
        }
    }
}


private[hano]
class FromTraversableOnce[A](_1: scala.collection.TraversableOnce[A]) extends Seq[A] {
    override def context = Self

    override def forloop(f: Reaction[A]) {
        context.onEnter {
            f.enter(_)
        } onEach { _ =>
            _1.foreach(f(_))
        } onExit {
            f.exit(_)
        } start()
    }
}

private[hano]
class ToTraversable[A](_1: Seq[A]) extends scala.collection.Traversable[A] {
    override def foreach[U](f: A => U) = _1.foreach(f(_))
}


private[hano]
class ToIterable[A](_1: Seq[A]) extends Iterable[A] {
    override def iterator = {
        if (_1.context eq Self) {
            new SyncIterable(_1).iterator
        } else {
            new AsyncIterable(_1).iterator
        }
    }
}


private[hano]
class FromResponder[A](_1: Responder[A]) extends Seq[A] {
    override def context = Self

    override def forloop(f: Reaction[A]) {
        context.onEnter {
            f.enter(_)
        } onEach { _ =>
            _1.respond(f(_))
        } onExit {
            f.exit(_)
        } start()
    }
}

private[hano]
class ToResponder[A](_1: Seq[A]) extends Responder[A] {
    override def respond(f: A => Unit) = _1.foreach(f)
}


private[hano]
class FromCps[A](from: => A @continuations.suspendable) extends Seq[A] {
    override def context = Self

    override def forloop(f: Reaction[A]) {
        context.onEnter {
            f.enter(_)
        } onEach { _ =>
            continuations.reset {
                f(from)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
