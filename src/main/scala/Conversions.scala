

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private[hano] trait Conversions { self: Seq.type =>

    @Annotation.returnThat
    def from[A](that: Seq[A]): Seq[A] = that

    implicit def fromArray[A](from: Array[A]): Seq[A] = new FromIter(from)
    implicit def fromIter[A](from: util.Iter[A]): Seq[A] = new FromIter(from)
    implicit def fromIterable[A](from: scala.collection.Iterable[A]): Seq[A] = new FromIter(from)
    implicit def fromTraversableOnce[A](from: scala.collection.TraversableOnce[A]): Seq[A] = new FromTraversableOnce(from)
    implicit def fromJIterable[A](from: java.lang.Iterable[A]): Seq[A] = new FromIter(from)
    implicit def fromOption[A](from: Option[A]): Seq[A] = new FromIter(from)
    implicit def fromResponder[A](from: Responder[A]): Seq[A] = new FromResponder(from)
    implicit def fromReactor(from: Reactor): Seq[Any] = new Reactor.Secondary(from)
    /*implicit*/ def fromCps[A](from: => A @scala.util.continuations.suspendable): Seq[A] = new FromCps(from)

}


private class FromIter[A](_1: util.Iter[A]) extends Seq[A] {
    @volatile private[this] var isActive = false
    override def close() = isActive = false
    override def forloop(f: A => Unit, k: Exit => Unit) = synchronized {
        assert(!isActive)
        isActive = true
        Exit.tryCatch(k) {
            val it = _1.begin
            while (isActive && it.hasNext) {
                f(it.next)
            }
        }
        if (isActive) {
            isActive = false
            k(Exit.End)
        }
    }
}


private class FromTraversableOnce[A](_1: scala.collection.TraversableOnce[A]) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            _1.foreach(f)
        }
        k(Exit.End)
    }
}

private class ToTraversable[A](_1: Seq[A]) extends scala.collection.Traversable[A] {
    override def foreach[U](f: A => U) = _1.foreach(x => f(x))
}


private class ToIterable[A](_1: Seq[A]) extends Iterable[A] {
    override def iterator = {
        util.Generator[A] { y =>
            _1.forloop(y, _ => y.exit())
        } iterator
    }
}


private class FromResponder[A](_1: Responder[A]) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            _1.respond(f)
        }
        k(Exit.End)
    }
}

private class ToResponder[A](_1: Seq[A]) extends Responder[A] {
    override def respond(f: A => Unit) = _1.foreach(f)
}


import scala.util.continuations

private class FromCps[A](from: => A @continuations.suspendable) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        continuations.reset {
            f(from)
        }
    }
}
