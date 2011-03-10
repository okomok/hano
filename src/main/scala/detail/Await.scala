

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent.{CountDownLatch, TimeUnit}


private[hano]
object Await {
    def apply[A](xs: Seq[A]) {
        val c = new CountDownLatch(1)
        var s: Throwable = null

        xs onExit { q =>
            CountDown(c) {
                q match {
                    case Exit.Failure(break.Control) => ()
                    case Exit.Failure(t) => s = t
                    case _ => ()
                }
            }
        } start()

        c.await()
        if (s ne null) {
            throw s
        }
    }

    def within[A](xs: Seq[A], _timeout: Long, _unit: TimeUnit): Boolean = {
        val c = new CountDownLatch(1)
        var s: Throwable = null

        xs onExit { q =>
            CountDown(c) {
                q match {
                    case Exit.Failure(break.Control) => ()
                    case Exit.Failure(t) => s = t
                    case _ => ()
                }
            }
        } start()

        val that = c.await(_timeout, _unit)
        if (that && (s ne null)) {
            throw s
        }
        that
    }
}
