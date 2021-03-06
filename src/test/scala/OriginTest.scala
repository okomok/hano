

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class OriginTest extends org.scalatest.junit.JUnit3Suite {

    def testThreaded { // force to create a thread.
        val c = new java.util.concurrent.CountDownLatch(1)
        val a = new java.util.ArrayList[Int]
        for (x <- hano.async.pull(0 until 10).onExit(_ =>c.countDown)) {
            a.add(x)
        }
        c.await
        expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
    }

    def testAsync { // in the thread pool.
        val c = new java.util.concurrent.CountDownLatch(1)
        val a = new java.util.ArrayList[Int]
        for (x <- hano.async.pull(0 until 10).onExit(_ =>c.countDown)) {
            a.add(x)
        }
        c.await
        expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
    }

    /* multiple-foreach-ness rejected
    def testMultipleForloop { // in the thread pool.
        val s = hano.async
        locally {
            val c = new java.util.concurrent.CountDownLatch(1)
            val a = new java.util.ArrayList[Int]
            for (x <- s.pull(0 until 10).onExit(_ =>c.countDown)) {
                a.add(x)
            }
            c.await
            expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
        }

        locally {
            val c = new java.util.concurrent.CountDownLatch(1)
            val a = new java.util.ArrayList[Int]
            for (x <- s.pull(0 until 10).onExit(_ =>c.countDown)) {
                a.add(x)
            }
            c.await
            expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
        }
    }
    */
}


class OriginStrictTest  extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Self.pull(hano.Iter(9,8,7,6,5))) {
            s.add(x)
        }
        assertEquals(hano.Iter(9,8,7,6,5), hano.Iter.from(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Self.pull(hano.Iter().of[Int])) {
            s.add(x)
        }
        assertTrue(s.isEmpty)
    }

    def testRandom: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Self.pull(Stream.continually(scala.math.random.toInt)).take(3)) {
            s.add(x)
            //println(x)
        }
        assertEquals(3, s.size)
    }
}
