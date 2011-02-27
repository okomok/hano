

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class BreakableTest extends org.scalatest.junit.JUnit3Suite {

    def naturals: hano.Seq[Int] = {
        hano.async.loop.pull(0 until 1000)
    }

    def testTrivial {
        val b = new java.util.ArrayList[Int]
        val c = new java.util.concurrent.CountDownLatch(1)

        naturals.foreach { x =>
            if (x == 50) {
                c.countDown()
                hano.break()
            } else {
                b.add(x)
            }
        }

        c.await()
        expect(hano.Iter.from(0 until 50))(hano.Iter.from(b))
    }

}
