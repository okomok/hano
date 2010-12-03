

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._
import hano.eval


class OriginTest extends org.scalatest.junit.JUnit3Suite {

    def testThreaded { // force to create a thread.
        val c = new java.util.concurrent.CountDownLatch(1)
        val a = new java.util.ArrayList[Int]
        for (x <- hano.Seq.origin(hano.eval.Threaded).generate(0 until 10).onExit(_ =>c.countDown)) {
            a.add(x)
        }
        c.await
        expect(0 until 10)(hano.util.Vector.make(a))
    }

    def testAsync { // in the thread pool.
        val c = new java.util.concurrent.CountDownLatch(1)
        val a = new java.util.ArrayList[Int]
        for (x <- hano.Seq.origin(hano.eval.Async).generate(0 until 10).onExit(_ =>c.countDown)) {
            a.add(x)
        }
        c.await
        expect(0 until 10)(hano.util.Vector.make(a))
    }

}


class OriginStrictTest  extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq.origin(hano.eval.Strict).generate(hano.util.Vector(9,8,7,6,5))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,8,7,6,5), hano.util.Vector.make(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq.origin(hano.eval.Strict).generate(hano.util.Vector.emptyOf[Int])) {
            s.add(x)
        }
        assertTrue(s.isEmpty)
    }

    def testRandom: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq.origin(hano.eval.Strict).generate(Stream.continually(scala.math.random.toInt)).take(3)) {
            s.add(x)
            //println(x)
        }
        assertEquals(3, s.size)
    }
}
