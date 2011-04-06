

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

        Polling.expect(1 to 50 toList, hano.Iter.from(q).able.toList.sortWith(_ < _))
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

        Polling.expect(hano.Iter(9,9,9,9,9,9,9,9,9,9,9,9), hano.Iter.from(q))
    }

    def testAddAll {
        val p = hano.async
        val xs = p.pull(0 until 3)
        val ys = p.pull(10 until 13)
        val rist = new hano.Rist[Int]
        rist addAll xs addAll ys
        Thread.sleep(1000)
        rist.exit()
        expect(List(0,1,2,10,11,12))(rist.toList.sortWith(_ < _))
    }

    def testAssign {
        val xs = hano.async.pull(0 until 100)
        val rist = new hano.Rist[Int]
        rist := xs

        expect(hano.Iter.from(0 until 100))(rist.toIter)
    }
}
