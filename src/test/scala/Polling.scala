

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano
import java.util.concurrent.TimeoutException


object Polling {
    private[this] val _timer = new hano.Timer(false)

    def assert(assertion: => Boolean, period: Long = 10L, timeout: Long = 5000L) {
        val res = {
            _timer.schedule(0, period).onEach { _ =>
                if (assertion) {
                    hano.break()
                }
            }.await(timeout)
        }
        if (!res) {
            throw new TimeoutException("Polling.assert")
        }
    }

    def expect(expected: Any, actual: => Any, period: Long = 10L, timeout: Long = 5000L) {
        var latest: Option[Any] = None
        val res = {
            _timer.schedule(0, period).onEach { _ =>
                val a = actual
                if (expected == a) {
                    hano.break()
                }
                latest = Some(a)
            }.await(timeout)
        }
        if (!res) {
            throw new TimeoutException("Polling.expect: " + expected + ", but actual: " + latest)
        }
    }
}


class PollingTest extends org.scalatest.junit.JUnit3Suite {

    def testAssertPass {
        val xs = hano.Timer.nondaemon.schedule(0, 100).pull(0 until 10)
        var res: Option[Int] = None
        xs.sum.onEach { x =>
            res = Some(x)
        }.start()
        Polling.assert(res.getOrElse(99) == 45)
    }

    def testAssertFailed {
        val xs = hano.Timer.nondaemon.schedule(0, 100).pull(0 until 10)
        var res: Option[Int] = None
        xs.sum.onEach { x =>
            res = Some(x)
        }.start()
        intercept[TimeoutException] {
            Polling.assert(res.getOrElse(99) == 35, timeout = 2000L)
        }
    }

    def testExpectPass {
        val xs = hano.Timer.nondaemon.schedule(0, 100).pull(0 until 10)
        var res: Option[Int] = None
        xs.sum.onEach { x =>
            res = Some(x)
        }.start()
        Polling.expect(Some(45), res)
    }

    def testExpectFailed {
        val xs = hano.Timer.nondaemon.schedule(0, 100).pull(0 until 10)
        var res: Option[Int] = None
        xs.sum.onEach { x =>
            res = Some(x)
        }.start()
        try {
            Polling.expect(Some(44), res, timeout = 2000L)
        } catch {
            case t: TimeoutException => expect("java.util.concurrent.TimeoutException: Polling.expect: Some(44), but actual: Some(Some(45))")(t.toString)
        }
    }
}
