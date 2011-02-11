

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors


private[hano]
class Async(out: actors.OutputChannel[Any] = Async.defaultOut) extends Context {
    override def exit() {
        out ! Exit.Closed
    }
    override def forloop(f: Reaction[Unit]) {
        out ! Action {
            try {
                Self.forloop(f)
            } catch {
                case t: Throwable => LogErr(t, "Reaction.apply error in async context")
            }
        }
    }
}


private[hano]
object Async {

    private[hano]
    def defaultOut: actors.OutputChannel[Any] = {
        val that = new DefaultOut
        that.start()
        that
    }

    private class DefaultOut extends actors.Reactor[Any] {
        override def act = {
            loop {
                react {
                    case Action(f) => f()
                    case _: Exit => exit()
                }
            }
        }
    }
}
