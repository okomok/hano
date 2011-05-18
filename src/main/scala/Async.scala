

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.actors


/**
 * An asynchronous process.
 * `out` shall react on `Action` and `Close` message.
 */
class Async(val out: actors.OutputChannel[Any] = new Async.Out().start) extends Process {
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


object Async {

    /**
     * Trivial `OutputChannel` implementation for `Async`
     */
    class Out(s: actors.IScheduler = null) extends actors.Reactor[Any] {
        override val scheduler = if (s eq null) super.scheduler else s
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
