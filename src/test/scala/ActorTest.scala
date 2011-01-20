

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import scala.actors.Actor


class ActorTest extends org.scalatest.junit.JUnit3Suite {

    case class Mail(msg: String)

    def testIdentity {

        val a1 = Actor.self
        Actor.actor {
            a1 ! Mail("to a1")
        }

        val a2 = Actor.self
        a2.receive {
            case Mail("to a1") => ()
        }
    }
}
