

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.actors.Actor


private[hano]
class Async() extends Context {

    private val a = new Actor {
        override def act = {
            Actor.loop {
                react {
                    case Body(f) => f()
                    case q: Exit => Actor.exit
                }
            }
        }
    }
    a.start()

    override def exit(q: Exit) { a ! q }

    override def forloop(f: Reaction[Unit]) {
        a ! Body {
            var thrown = false
            try {
                f()
            } catch {
                case t: Throwable => {
                    thrown = true
                    LogErr(t, "Reaction.apply error in async context")
                    f.exitCatch(Exit.Failed(t))
                }
            }
            if (!thrown) {
                f.exitCatch(Exit.End)
            }
        }
    }
}

