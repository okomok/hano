

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


private class Invoke[R](_1: Function0[R], _2: Runnable => Unit) extends Function0[R] {
    private[this] var r: Either[Throwable, R] = null
    private[this] val c = new java.util.concurrent.CountDownLatch(1)
    _2 {
        new Runnable {
            override def run() {
                try {
                    r = Right(_1())
                } catch {
                    case t: Throwable => r = Left(t)
                } finally {
                    c.countDown()
                }
            }
        }
    }
    override def apply = {
        c.await()
        r match {
            case Right(r) => r
            case Left(t) => throw t
        }
    }
}
