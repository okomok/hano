

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


object PImpl {

    import hano._
    import java.util.concurrent.{CountDownLatch, TimeUnit}

    object Await {
        def apply[A](xs: Seq[A], timeout: Long): Boolean = {
            val c = new CountDownLatch(1)
            var s: Throwable = null

            var _p = Exit.Empty.asExit

            async.invoke {
                xs.onEnter { p =>
                    _p = p
                } onExit { q =>
                    Util.countDown(c) {
                        q match {
                            case Exit.Failure(break.Control) => ()
                            case Exit.Failure(Exit.Interrupted(_)) => ()
                            case Exit.Failure(t) => s = t
                            case _ => ()
                        }
                    }
                } start()
            }

            if (timeout < 0) {
                c.await()
                _p()
                if (s ne null) {
                    throw s
                }
                true
            } else {
                val that = c.await(timeout, TimeUnit.MILLISECONDS)
                _p()
                if (that && (s ne null)) {
                    throw s
                }
                that
            }
        }
    }

}

object PollAssert {
    private[this] val _timer = new hano.Timer(false)

    def apply(assertion: => Boolean, period: Long = 10L, timeout: Long = 1000L) {
        PImpl.Await(_timer.schedule(0, period).shift {
            hano.Self
        }.onEach { _ =>
            //println("checking")
            if (assertion) {
                hano.break()
            }
        }, timeout)
    }
}


class PollAssertTest extends org.scalatest.junit.JUnit3Suite {

    def testPass {
        val xs = hano.Timer.nondaemon.schedule(0, 100).pull(0 until 10)
        var res: Option[Int] = None
        xs.sum.onEach { x =>
            res = Some(x)
        }.start()
        PollAssert(res.getOrElse(99) == 45)
    }

    def testFailed {
        val xs = hano.Timer.nondaemon.schedule(0, 100).pull(0 until 10)
        var res: Option[Int] = None
        xs.sum.onEach { x =>
            res = Some(x)
        }.start()
        PollAssert(res.getOrElse(99) == 35)
    }

}
