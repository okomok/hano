

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Exit

object Exit {

    case object End extends Exit

    // case object Closed extends Exit // feasible?

    case class Thrown(what: Throwable) extends Exit

    private[hano] def tryCatch(k: Exit => Unit)(body: => Unit) {
        try {
            body
        } catch {
            case t: Throwable => {
                k(Thrown(t)) // informs reaction-site
                throw t // Seq-site responsibility
            }
        }
    }

    private[hano] val defaultHandler: Exit => Unit = { _ => () }
/*
    private[hano] val defaultHandler: Exit => Unit = {
        case Thrown(t) => {
            val d = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date())
            java.lang.System.err.println("[hano.Exit.Thrown]["+ d + "] " + t.toString)
        }
        case _ => ()
    }
*/
}
