

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Exit extends Message


object Exit {

    case object End extends Exit

    case object Closed extends Exit

    case class Failed(why: Throwable) extends Exit

    private[hano]
    object defaultHandler extends (Exit => Unit) {
        override def apply(q: Exit) = q match {
            case Exit.Failed(t) => detail.LogErr(t, "unhandled failure")
            case _ => ()
        }
    }
}
