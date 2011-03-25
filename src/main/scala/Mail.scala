

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Mail[+A] {
    @annotation.returnThis @inline
    def asMail: Mail[A] = this

    def isElement: Boolean

    def isExit: Boolean

    def element: A

    def toOption: Option[A] = this match {
        case EnterMail(_) => None
        case ElementMail(x) => Some(x)
        case ExitMail(Exit.Failure(t)) => throw t
        case ExitMail(_) => None
    }

    def evalBy(f: Reaction[A]) = this match {
        case EnterMail(p) => f.enter(p)
        case ElementMail(x) => f(x)
        case ExitMail(q) => f.exit(q)
    }
}


case class EnterMail(exit: Exit) extends Mail[Nothing] {
    override def isElement = false
    override def isExit = false
    override def element = throw new NoSuchElementException("EnterMail.element")
}

case class ElementMail[A](override val element: A) extends Mail[A] {
    override def isElement = true
    override def isExit = false
}

case class ExitMail(status: Exit.Status) extends Mail[Nothing] {
    override def isElement = false
    override def isExit = true
    override def element = status match {
        case Exit.Success => throw new NoSuchElementException("ExitMail.element")
        case Exit.Failure(t) => throw t
    }
}


private[hano]
class SeqMail[A](_1: Seq[A]) extends SeqAdapter.Of[Mail[A]](_1) {
    override def forloop(f: Reaction[Mail[A]]) {
        _1.onEnter { p =>
            f.enter(p)
            f(EnterMail(p))
        } onEach { x =>
            f(ElementMail(x))
        } onExit { q =>
            f(ExitMail(q))
            f.exit(q)
        } start()
    }
}

private[hano]
class SeqUnmail[A](_1: Seq[Mail[A]]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach {
            case EnterMail(p) => f.enter(p)
            case ElementMail(x) => f(x)
            case ExitMail(q) => f.exit(q)
        } onExit {
            f.exit(_)
        } start()
    }
}
