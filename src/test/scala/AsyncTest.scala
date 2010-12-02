

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano




class AsyncTest extends org.scalatest.junit.JUnit3Suite {

    def naturals: hano.Seq[Int] = {
        hano.Seq.origin(hano.eval.Async).generate(0 until 1000)
    }

    def testTrivial {
        val b = new java.util.ArrayList[Int]
        val c = new java.util.concurrent.CountDownLatch(1)

        naturals take {
            100
        } onExit { _ =>
            c.countDown
        } foreach {
            b.add(_)
        }

        c.await
        expect(0 until 100)(hano.util.Iterable.from(b))
    }

    def testRejection {
        val b = new java.util.ArrayList[Int]
        val c = new java.util.concurrent.CountDownLatch(1)

        var _break = false
        while (!_break) {
            try {
                hano.eval.Parallel.or(hano.eval.Reject) {
                    Thread.sleep(7)
                }
            } catch {
                case _: hano.eval.RejectedException => _break = true
            }
        }

        naturals take {
            100
        } onExit { _ =>
            c.countDown
        } foreach { x =>
            b.add(x)
        }

        c.await
        expect(0 until 100)(hano.util.Iterable.from(b))
    }

    def testReallyLazyVal {
        def anError = hano.Seq.origin(hano.eval.Async).generate(hano.util.Iterable.lazySingle{throw new Error; 999})
        anError take 100
        ()
    }

}
