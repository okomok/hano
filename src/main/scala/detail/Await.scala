

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Await {
    def apply[A](xs: Seq[A]) {
        val c = new java.util.concurrent.CountDownLatch(1)
        var s: Throwable = null

        xs onExit { q =>
            CountDown(c) {
                q match {
                    case Exit.Failed(break.Control) => ()
                    case Exit.Failed(t) => s = t
                    case _ => ()
                }
            }
        } start()

        c.await()
        if (s != null) {
            throw s
        }
    }
}
