

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano




class BreakableTest extends org.scalatest.junit.JUnit3Suite {

    def naturals: hano.Seq[Int] = {
        hano.Seq.origin(hano.eval.Async).generate(0 until 1000)
    }

    def testTrivial {
        val b = new java.util.ArrayList[Int]
        val c = new java.util.concurrent.CountDownLatch(1)

        naturals.breakable.foreach { case (x, break) =>
            if (x == 50) {
                c.countDown()
                break()
            } else {
                b.add(x)
            }
        }

        c.await()
        expect(hano.util.Iter.from(0 until 50))(hano.util.Iter.from(b))
    }

}
