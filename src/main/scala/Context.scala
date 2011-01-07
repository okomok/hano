

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.TimerTask
import scala.actors.Actor
import detail.LogErr


trait Context extends Seq[Unit] with Reaction[() => Unit] {
    final override def close() = ()
    final override def context = this

    final def loop: Seq[Unit] = new detail.Loop(this)

    final override def apply(body: () => Unit) {
        foreach(_ => body())
    }
    final def eval(body: => Unit) {
        apply(() => body)
    }

    private[hano]
    final def upper(that: Context): Context = {
        if (this eq Context.self) {
            if (that eq Context.self) {
                this
            } else {
                that
            }
        } else {
            this
        }
    }
}


object Context {


    /**
     * In the call-site
     */
    val self: Context = new detail.Self()

    /**
     * In the thread-pool
     */
    def async: Context = new detail.Async()

    /**
     * In the event-dispatch-thread
     */
    val inEdt: Context = new detail.InEdt()

    /**
     * In a timer
     */
    //def inTimer(schedule: TimerTask => Unit): Seq[Unit] = new InTimer(schedule)


    /*

    private class InTimer(_2: TimerTask => Unit) extends SeqProxy[Unit] with Context {
        override def context = this
        override val self = origin { body =>
            var l: TimerTask = null
            l = new TimerTask {
                override def run() = {
                    body
                    l.cancel()
                }
            }
            _2(l)
        }
    }
*/


    /*

    Something bad happens.


    private def origin(eval: (=> Unit) => Unit): Seq[Unit] = origin(eval)

    private class Origin(_1: (=> Unit) => Unit) extends Seq[Unit] with Context {
        override def context: Seq[Unit] = this
        override def forloop(f: Reaction[Unit]) {
            _1 {
                f.tryRethrow(context) {
                    f()
                }
                f.exit(Exit.End)
            }
        }
    }

    private class Self() extends SeqProxy[Unit] with Context {
        override def context = this
        override lazy val self = origin { body =>
            body
        }
    }

    private class Async() extends SeqProxy[Unit] with Context {
        override def context = this
        override def close() {
            a ! Exit.Closed
        }
        override val self = origin { body =>
            a ! Task(() => body)
        }
        private val a = new Actor {
            override def act = {
                Actor.loop {
                    react {
                        case Task(f) => f()
                        case q: Exit => Actor.exit
                    }
                }
            }
        }
        a.start()
    }

    private class InEdt() extends SeqProxy[Unit] with Context {
        override def context = this
        override lazy val self = origin { body =>
            javax.swing.SwingUtilities.invokeLater {
                new Runnable {
                    override def run() = body
                }
            }
        }
    }
    */

}
