

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors.Actor


private[hano]
class Act() extends Context {

    private val a = new Actor {
        override def act = {
            Actor.loop {
                react {
                    case Action(f) => f()
                    case _: Exit => Actor.exit
                }
            }
        }
    }
    a.start()

    override def exit() {
        a ! Exit.Closed
    }

    override def forloop(f: Reaction[Unit]) {
        a ! Action {
            try {
                Context.self.forloop(f)
            } catch {
                case t: Throwable => LogErr(t, "Reaction.apply error in act context")
            }
        }
    }
}

