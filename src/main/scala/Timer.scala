

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.{Date, Timer => JTimer, TimerTask}


object Timer {
    /**
     * The global timer in daemon thread
     */
    lazy val daemon = new Timer(true)

    /**
     * The global timer in non-daemon thread
     */
    lazy val nondaemon = new Timer(false)
}


/**
 * Creates a sequence of Units.
 */
final class Timer(isDaemon: Boolean = false) extends Context { outer =>
    private[this] val timer = new JTimer(isDaemon)
    private[this] val now = new detail.Now

    override def close() = timer.cancel()

    override def forloop(f: Reaction[Unit]) {
        val l = new TimerTask {
            override def run() {
                try {
                    Self.forloop(f)
                } catch {
                    case t: Throwable => detail.LogErr(t, "Reaction.apply error in Timer context")
                }
            }
        }

        timer.schedule(l, now())
    }

    private class Schedule(scheduler: JTimer => TimerTask => Unit, oneShot: Boolean) extends listen.To[Unit] {
        override def context = outer.asContext
        override protected def listen(env: Env) {
            val l = new TimerTask {
                override def run() = {
                    env()

                    if (oneShot) {
                        env.exit(Exit.Success)
                    }
                }
            }

            env.add {
                scheduler(timer)(l)
            }
            env.remove {
                l.cancel()
            }
        }
    }

    def schedule(time: Date): Seq[Unit] = new Schedule(ti => ta => ti.schedule(ta, time), true)
    def schedule(firstTime: Date, period: Long): Seq[Unit] = new Schedule(ti => ta => ti.schedule(ta, firstTime, period), false)
    def schedule(delay: Long): Seq[Unit] = new Schedule(ti => ta => ti.schedule(ta, delay), true)
    def schedule(delay: Long, period: Long): Seq[Unit] = new Schedule(ti => ta => ti.schedule(ta, delay, period), false)
    def scheduleAtFixedRate(firstTime: Date, period: Long): Seq[Unit] = new Schedule(ti => ta => ti.scheduleAtFixedRate(ta, firstTime, period), false)
    def scheduleAtFixedRate(delay: Long, period: Long): Seq[Unit] = new Schedule(ti => ta => ti.scheduleAtFixedRate(ta, delay, period), false)
}
