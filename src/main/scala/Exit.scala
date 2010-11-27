

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Exit


object Exit {

    case object End extends Exit
    case object Closed extends Exit
    case class Thrown(what: Throwable) extends Exit

    def tryCatch(q: Exit => Unit)(body: => Unit) {
        try {
            body
        } catch {
            case t: Throwable => {
                q(Thrown(t))
                throw t
            }
        }
    }
}
