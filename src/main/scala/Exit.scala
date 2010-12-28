

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Exit


object Exit {

    case object End extends Exit

    case object Closed extends Exit

    case class Failed(why: Throwable) extends Exit

    private[hano]
    def tryCatch(f: Reaction[_])(body: => Unit) {
        try {
            body
        } catch {
            case t: Throwable => {
                f.exit(Failed(t)) // informs Reaction-site
                throw t // handled in Seq-site
            }
        }
    }
/*
    private[hano]
    val defaultHandler: Exit => Unit = { _ => () }

    private[hano]
    val defaultHandler: Exit => Unit = {
        case Failed(t) => {
            val d = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date())
            java.lang.System.err.println("[hano.Exit.Failed]["+ d + "] " + t.toString)
        }
        case _ => ()
    }
*/
}
