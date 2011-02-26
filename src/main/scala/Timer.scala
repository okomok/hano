

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.{Date, Timer => JTimer, TimerTask}


/**
 * Timer creates a sequence of Units.
 */
final class Timer(isDaemon: Boolean = false) extends Context { outer =>

    private[this] val timer = new JTimer(isDaemon)
    private[this] val zero = new detail.ZeroDelay

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
        timer.schedule(l, zero())
    }

    private class Schedule(scheduler: JTimer => TimerTask => Unit) extends Seq[Unit] {
        override def context = outer.asContext
        override protected def openResource(f: Reaction[Unit]) {
            val l = new TimerTask {
                override def run() {
                    f.enter { l.cancel() }
                    f._do { f() }
            }
            scheduler(timer)(l)
        }
    }

    def schedule(time: Date): Seq[Unit] = new Schedule(ti => ta => ti.schedule(ta, time))
    def schedule(firstTime: Date, period: Long): Seq[Unit] = new Schedule(ti => ta => ti.schedule(ta, firstTime, period))
    def schedule(delay: Long): Seq[Unit] = new Schedule(ti => ta => ti.schedule(ta, delay))
    def schedule(delay: Long, period: Long): Seq[Unit] = new Schedule(ti => ta => ti.schedule(ta, delay, period))
    def scheduleAtFixedRate(firstTime: Date, period: Long): Seq[Unit] = new Schedule(ti => ta => ti.scheduleAtFixedRate(ta, firstTime, period))
    def scheduleAtFixedRate(delay: Long, period: Long): Seq[Unit] = new Schedule(ti => ta => ti.scheduleAtFixedRate(ta, delay, period))
}
