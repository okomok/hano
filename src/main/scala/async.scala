

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.actors


/**
 * Thread-pool context
 */
object async {

    def apply(out: actors.OutputChannel[Any] = defaultOut): Context = new Impl(out)

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

    private[hano]
    class Impl(out: actors.OutputChannel[Any]) extends Context {
        override def exit() {
            out ! Exit.Closed
        }
        override def forloop(f: Reaction[Unit]) {
            out ! Action {
                try {
                    Self.forloop(f)
                } catch {
                    case t: Throwable => detail.LogErr(t, "Reaction.apply error in async context")
                }
            }
        }
    }
}
