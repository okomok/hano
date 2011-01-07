

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors.Actor


case class AsyncTask(_1: () => Unit)


private[hano]
class Async() extends Context {

    private val a = new Actor {
        override def act = {
            Actor.loop {
                react {
                    case AsyncTask(f) => f()
                    case q: Exit => Actor.exit
                }
            }
        }
    }
    a.start()

    override def exit(q: Exit) { a ! q }

    override def forloop(f: Reaction[Unit]) {
        a ! AsyncTask { () =>
            var thrown = false
            try {
                f()
            } catch {
                case t: Throwable => {
                    thrown = true
                    LogErr(t, "Reaction.apply error in async context")
                    try {
                        f.exit(Exit.Failed(t))
                    } catch {
                        case t: Throwable => LogErr(t, "Reaction.exit error in async context")
                    }
                }
            }
            if (!thrown) {
                try {
                    f.exit(Exit.End)
                } catch {
                    case t: Throwable => LogErr(t, "Reaction.exit error in async context")
                }
            }
        }
    }
}

