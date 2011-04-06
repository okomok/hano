

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors


private[hano]
class Async(out: actors.OutputChannel[Any] = Async.defaultOut) extends Process {
    override def close() {
        out ! Close
    }

    override def `do`(f: Reaction[Unit]) {
        out ! Action {
            try {
                Self.`do`(f)
            } catch {
                case t: Throwable => detail.Log.err("async process", t)
            }
        }
    }
}


private[hano]
object Async {
    private[hano]
    def defaultOut: actors.OutputChannel[Any] = {
        val res = new DefaultOut
        res.start()
        res
    }

    private class DefaultOut extends actors.Reactor[Any] {
        override def act = {
            loop {
                react {
                    case Action(f) => f()
                    case Close => exit()
                }
            }
        }
    }
}
