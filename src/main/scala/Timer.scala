

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.TimerTask


case class Schedule(scheduler: TimerTask => Unit) extends NoExitResource[Unit] {
    private[this] var l: TimerTask = null
    override protected def closeResource() = l.cancel()
    override protected def openResource(f: Unit => Unit) {
        l = new TimerTask {
            override def run() = f()
        }
        scheduler(l)
    }
}
