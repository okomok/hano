

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


object PollAssert {
    private[this] val _timer = new hano.Timer(false)

    def apply(assertion: => Boolean, period: Long = 10L, timeout: Long = 3000L) {
        val res = {
            _timer.schedule(0, period).onEach { _ =>
                if (assertion) {
                    hano.break()
                }
            }.await(timeout)
        }
        if (!res) {
            throw new java.util.concurrent.TimeoutException("PollAssert")
        }
    }
}


class PollAssertTest extends org.scalatest.junit.JUnit3Suite {

    def testAssertPass {
        val xs = hano.Timer.nondaemon.schedule(0, 100).pull(0 until 10)
        var res: Option[Int] = None
        xs.sum.onEach { x =>
            res = Some(x)
        }.start()
        PollAssert(res.getOrElse(99) == 45)
    }

    def testAssertFailed {
        val xs = hano.Timer.nondaemon.schedule(0, 100).pull(0 until 10)
        var res: Option[Int] = None
        xs.sum.onEach { x =>
            res = Some(x)
        }.start()
        intercept[java.util.concurrent.TimeoutException] {
            PollAssert(res.getOrElse(99) == 35)
        }
    }
}
