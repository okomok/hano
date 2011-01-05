

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class MergeTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val r1 = hano.Seq(1,2,3)
        val r2 = hano.Seq(4,5)
        val out = new java.util.ArrayList[Int]
        for (x <- r1 merge r2) {
            out.add(x)
        }
        assertEquals(hano.Iter(1,2,3,4,5), hano.Iter.from(out))
    }

    def testNonTrivial: Unit = {
        val r1 = hano.Seq(1,2,3)
        val r2 = hano.Seq(4,5)
        val r3 = hano.Seq(6,7,8,9)
        val out = new java.util.ArrayList[Int]
        for (x <- r1 merge r2 merge r3) {
            out.add(x)
        }
        assertEquals(hano.Iter(1,2,3,4,5,6,7,8,9), hano.Iter.from(out))
    }

    def testDuplicate {
        val r = hano.Seq(1,2,3,4,5)
        val (r1, r2) = r.duplicate
        val out = new java.util.ArrayList[Int]
        for (x <- r1 merge r2) {
            out.add(x)
        }
        assertEquals(hano.Iter(1,1,2,2,3,3,4,4,5,5), hano.Iter.from(out))
    }

    def testEnd {
        val xs = hano.Context.async.loop.generate(0 to 5)
        val ys = hano.Context.async.loop.generate(6 to 9)
        val out = new Array[Int](10)
        var ends = false
        val gate = new java.util.concurrent.CountDownLatch(1)
        var i = 0
        var exitCount = 0
        for (x <- (xs merge ys).onExit{ case hano.Exit.End => { exitCount += 1; gate.countDown() }; case _ => fail("doh") }) {
            out(i) = x
            i += 1
        }
        gate.await()
        expect(1)(exitCount)
        java.util.Arrays.sort(out)
        assertEquals(hano.Iter(0,1,2,3,4,5,6,7,8,9), hano.Iter.from(out))
    }

    def testWhenThrown {
        val xs = hano.Context.async.loop.generate(0 until 5)
        val ys = hano.Context.async.loop.generate(5 until 1000)
        val out = new java.util.ArrayList[Int]
        var ends = false
        val gate = new java.util.concurrent.CountDownLatch(1)
         var exitCount = 0
        for (x <- (xs merge ys).onExit{ case hano.Exit.Failed(_) => { exitCount += 1; gate.countDown() }; case _ => fail("doh") }) {
            //println(x)
            if (x == 2) {
                throw new Error // disappears in Future.
            }
            Thread.sleep(3) // out.size will be smaller.
            out.add(x)
        }
        gate.await()
        expect(1)(exitCount)
        //println(hano.Iter.from(out))
        assert(out.size < 900)
        expect(None)(hano.Iter.from(out).able.find(_ == 3))
    }

}
