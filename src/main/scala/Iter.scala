

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.collection.JavaConversions


/**
 * Trivial wrapper for Iterators (used internally)
 */
trait Iter[+A] extends Equals {
    @annotation.returnThis
    final def of[B >: A]: Iter[B] = this

    def ator: Iterator[A]

    def able: Iterable[A] = new Iter.Able(this)

    @annotation.pre("finite if result is `true`")
    override def equals(that: Any) = that match {
        case that: Iter[_] => (that canEqual this) && equalsIf(that)(_ == _)
        case _ => false
    }

    override def canEqual(that: Any) = that.isInstanceOf[Iter[_]]

    @annotation.pre("finite")
    override def hashCode = {
        var r = 1
        val it = ator
        while (it.hasNext) {
            r = 31 * r + it.next.hashCode
        }
        r
    }

    @annotation.pre("finite")
    override def toString = {
        val sb = new StringBuilder
        sb.append('[')

        val it = ator
        if (it.hasNext) {
            sb.append(it.next)
        }
        while (it.hasNext) {
            sb.append(", ")
            sb.append(it.next)
        }

        sb.append(']')
        sb.toString
    }

    def equalsIf[B](that: Iter[B])(p: (A, B) => Boolean): Boolean = {
        val it = ator
        val jt = that.ator
        while (it.hasNext && jt.hasNext) {
            if (!p(it.next, jt.next)) {
                return false
            }
        }
        !it.hasNext && !jt.hasNext
    }
}


object Iter {
    def apply[A](xs: A*): Iter[A] = from(xs)

    @annotation.returnThat
    def from[A](that: Iter[A]): Iter[A] = that

    implicit def fromIterator[A](from: => Iterator[A]): Iter[A] = new FromIterator(() => from)
    implicit def fromIterable[A](from: Iterable[A]): Iter[A] = new FromIterable(from)
    implicit def fromJIterator[A](from: => java.util.Iterator[A]): Iter[A] = new FromJIterator(() => from)
    implicit def fromJIterable[A](from: java.lang.Iterable[A]): Iter[A] = new FromJIterable(from)
    implicit def fromArray[A](from: Array[A]): Iter[A] = new FromArray(from)
    implicit def fromOption[A](from: Option[A]): Iter[A] = new FromOption(from)
    implicit def fromCharSequence(from: java.lang.CharSequence): Iter[Char] = new FromCharSequence(from)

    private class FromIterator[A](_1: () => Iterator[A]) extends Iter[A] {
        override def ator = _1()
    }

    private class FromIterable[A](_1: Iterable[A]) extends Iter[A] {
        override def ator = _1.iterator
    }

    private class FromJIterator[A](_1: () => java.util.Iterator[A]) extends Iter[A] {
        import JavaConversions._
        override def ator = _1()
    }

    private class FromJIterable[A](_1: java.lang.Iterable[A]) extends Iter[A] {
        import JavaConversions._
        override def ator = _1.iterator
    }

    private class FromArray[A](_1: Array[A]) extends Iter[A] {
        override def ator = _1.iterator
    }

    private class FromOption[A](_1: Option[A]) extends Iter[A] {
        override def ator = _1.iterator
    }

    private class FromCharSequence(_1: java.lang.CharSequence) extends Iter[Char] {
        override def ator = new detail.AbstractIterator[Char] {
            private[this] var i = 0
            override def isEnd = i == _1.length
            override def deref = _1.charAt(i)
            override def increment() = i += 1
        }.concrete
    }

    private class Able[A](_1: Iter[A]) extends Iterable[A] {
        override def iterator = _1.ator
    }
}
