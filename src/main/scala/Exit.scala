

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Exit

case object End extends Exit

case object Closed extends Exit // TODO?

case class Thrown(what: Throwable) extends Exit


object Exit {

     val End = hano.End
     val Closed = hano.Closed
     val Thrown = hano.Thrown
    type Thrown = hano.Thrown

    def tryCatch(k: Exit => Unit)(body: => Unit) {
        try {
            body
        } catch {
            case t: Throwable => {
                k(Thrown(t))
                throw t
            }
        }
    }

}
