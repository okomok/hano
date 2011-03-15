

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
sealed abstract class Mail[+A] {
    @annotation.returnThis @inline
    def asMail: Mail[A] = this

    def isExit: Boolean

    def element: A

    def toOption: Option[A] = this match {
        case ElementMail(x) => Some(x.asInstanceOf[A])
        case ExitMail(Exit.Failure(t)) => throw t
        case ExitMail(_) => None
    }
}

private[hano]
case class ElementMail[A](override val element: A) extends Mail[A] {
    override def isExit = false
}

private[hano]
case class ExitMail(status: Exit.Status) extends Mail[Nothing] {
    override def isExit = true
    override def element = status match {
        case Exit.Success => throw new NoSuchElementException("ExitMail.element")
        case Exit.Failure(t) => throw t
    }
}
