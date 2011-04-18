

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent.{CountDownLatch, TimeUnit}


private[hano]
object Await {
    def apply[A](xs: Seq[A], timeout: Long): Boolean = {
        if (timeout < 0) {
            forever(xs)
        } else {
            within(xs, timeout)
        }
    }

    private def forever[A](xs: Seq[A]): Boolean = {
        val c = new CountDownLatch(1)
        var s: Throwable = null

        xs.onExit { q =>
            Util.countDown(c) {
                q match {
                    case Exit.Failure(BreakControl) => ()
                    case Exit.Failure(Exit.Interrupted(_)) => ()
                    case Exit.Failure(t) => s = t
                    case _ => ()
                }
            }
        } start()

        c.await()
        if (s ne null) {
            throw s
        }
        true
    }

    private def within[A](xs: Seq[A], timeout: Long): Boolean = {
        val c = new CountDownLatch(1)
        var s: Throwable = null
        val out = new Cancel

        xs.onEnter(out).onExit { q =>
            Util.countDown(c) {
                q match {
                    case Exit.Failure(BreakControl) => ()
                    case Exit.Failure(Exit.Interrupted(_)) => ()
                    case Exit.Failure(t) => s = t
                    case _ => ()
                }
            }
        } start()

        val res = c.await(timeout, TimeUnit.MILLISECONDS)
        if (res) { // in time
            if (s ne null) {
                throw s
            }
        } else {
            out(Exit.Failure(new java.util.concurrent.TimeoutException("aSeq.await")))
        }
        res
    }
}
