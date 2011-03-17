

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent.{CountDownLatch, TimeUnit}


private[hano]
object Await {
    def apply[A](xs: Seq[A], timeout: Long): Boolean = {
        val c = new CountDownLatch(1)
        var s: Throwable = null

        xs.onExit { q =>
            Util.countDown(c) {
                q match {
                    case Exit.Failure(break.Control) => ()
                    case Exit.Failure(Exit.ByOther(_)) => ()
                    case Exit.Failure(t) => s = t
                    case _ => ()
                }
            }
        } start()

        if (timeout < 0) {
            c.await()
            if (s ne null) {
                throw s
            }
            true
        } else {
            val that = c.await(timeout, TimeUnit.MILLISECONDS)
            if (that && (s ne null)) {
                throw s
            }
            that
        }
    }
}
