

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

class RistTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val xs = new hano.Rist[Int]

        val suite = new ParallelSuite(10)
        val i = new java.util.concurrent.atomic.AtomicInteger(0)
        suite.add(50) {
            xs add i.incrementAndGet
        }

        val q = new java.util.concurrent.ConcurrentLinkedQueue[Int]
        suite.add(1) {
            for (x <- xs) {
                q.offer(x)
            }
        }

        suite.start()

        Thread.sleep(2000)

        val arr = new java.util.ArrayList[Int]
        for (x <- hano.Iter.from(q).able) {
            arr.add(x)
        }
        java.util.Collections.sort(arr, implicitly[Ordering[Int]])
        expect(hano.Iter.from(1 to 50))(hano.Iter.from(arr))
    }

    def testTrivial2 {
        val xs = new hano.Rist[Int]

        val suite = new ParallelSuite(10)
        val i = new java.util.concurrent.atomic.AtomicInteger(0)
        suite.add(50) {
            xs add 9
        }

        val q = new java.util.concurrent.ConcurrentLinkedQueue[Int]
        suite.add(1) {
            for (x <- xs.take(12)) {
                q.offer(x)
            }
        }

        suite.start()

        Thread.sleep(2000)
        expect(hano.Iter(9,9,9,9,9,9,9,9,9,9,9,9))(hano.Iter.from(q))
    }

    def testAssign {
        val xs = hano.async.pull(0 until 100)
        val rist = hano.Rist(xs)

        expect(hano.Iter.from(0 until 100))(rist.toIter)
    }
}
