

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
sealed abstract class Mail[+A] {
    def toOption: Option[A] = this match {
        case ElemMail(x) => Some(x.asInstanceOf[A])
        case ExitMail(Exit.Failure(t)) => throw t
        case ExitMail(_) => None
    }
}

private[hano]
case class ElemMail[A](element: A) extends Mail[A]

private[hano]
case class ExitMail(status: Exit.Status) extends Mail[Nothing]

