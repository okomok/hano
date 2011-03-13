

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent.CountDownLatch


private[hano]
object Await {
    def apply[A](xs: Seq[A], t: Within): Boolean = {
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

        t match {
            case Within.Inf => {
                c.await()
                if (s ne null) {
                    throw s
                }
                true
            }
            case Within.Elapse(d, u) => {
                val that = c.await(d, u)
                if (that && (s ne null)) {
                    throw s
                }
                that
            }
        }
    }
}
