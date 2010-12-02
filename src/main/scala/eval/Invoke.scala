

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


private class Invoke[R](_1: Function0[R], _2: Runnable => Unit) extends Function0[R] {
    private[this] val c = new java.util.concurrent.CountDownLatch(1)
    private[this] var r: Either[R, Throwable] = null
    _2 {
        new Runnable {
            override def run() {
                try {
                    r = Left(_1())
                } catch {
                    case t: Throwable => r = Right(t)
                } finally {
                    c.countDown()
                }
            }
        }
    }
    private[this] lazy val v = {
        c.await()
        r match {
            case Left(r) => r
            case Right(t) => throw t
        }
    }
    override def apply = v
}
