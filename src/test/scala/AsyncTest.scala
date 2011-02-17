

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class AsyncTest extends org.scalatest.junit.JUnit3Suite {

    def naturals: hano.Seq[Int] = {
        hano.async.loop.pull(0 until 1000)
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
        expect(hano.Iter.from(0 until 100))(hano.Iter.from(b))
    }

    def testReallyLazy {
        val xs = hano.async.loop.pull(Iterator.continually(10)).take(3)

        expect(hano.Iter(10,10,10))(xs.toIter)
        expect(hano.Iter(10,10,10))(xs.toIter)
        expect(hano.Iter(10,10,10))(xs.toIter)
    }

/*
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
        expect(hano.Iter.from(0 until 100))(hano.Iter.from(b))
    }
*/
}
