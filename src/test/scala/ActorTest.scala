

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import scala.actors.{Actor, Reactor, ReplyReactor}

/*
class ActorTest extends org.scalatest.junit.JUnit3Suite {

    case class Mail(msg: String)

    def testShared {
        val a1 = Actor.self
        Actor.actor {
            a1 ! Mail("to a1")
        }

        val a2 = Actor.self
        a2.receive {
            case Mail("to a1") => ()
        }
    }

    def testChannel {
        val a1 = new scala.actors.Channel[Any]
        Actor.actor {
            a1 ! Mail("to a1")
        }

        val a2 = new scala.actors.Channel[Any]
        a2.receiveWithin(1000) {
            case Mail("to a1") => fail("doh")
            case scala.actors.TIMEOUT => ()
        }
    }

    def testSendSelf {
        val a = new Reactor[Any] {
            override def act() = {
                loop {
                    react {
                        case "Go" => {
                            println("Go")
                        }
                    }
                }
            }
        }

        a.start()
        a ! "Go"
        a ! "Go"
        a ! "Go"

        Thread.sleep(1000)
    }
}
*/
