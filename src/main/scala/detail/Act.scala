

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors.Actor


private[hano]
class Act(a: Actor = Act.defaultActor) extends Context {

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


private[hano]
object Act {

    def defaultActor: Actor = {
        val that = new Actor {
            override def act = {
                Actor.loop {
                    react {
                        case Action(f) => f()
                        case _: Exit => Actor.exit()
                    }
                }
            }
        }
        that.start
        that
    }
}
