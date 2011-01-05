

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.TimerTask


case class Schedule(_1: TimerTask => Unit) extends NoExitResource[Unit] {
    override def context = throw new Error("no context")
    @volatile private[this] var l: TimerTask = null
    override protected def closeResource() = l.cancel()
    override protected def openResource(f: Unit =>  Unit) {
        l = new TimerTask {
            override def run() = f()
        }
        _1(l)
    }
}
