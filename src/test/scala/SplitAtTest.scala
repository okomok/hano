

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class SplitAtTest extends org.scalatest.junit.JUnit3Suite {

    class TrivialResource extends hano.NoExitResource[Int] {
        var closed = false
        override protected def closeResource = closed = true
        override protected def openResource(f: Int => Unit) = {
            f(1); f(2); f(3); f(4); f(5)
        }
    }

    def testTrivial {
        val r = new TrivialResource
        val (xs, ys) = r.splitAt(2)
        val xa = new java.util.ArrayList[Int]
        val ya = new java.util.ArrayList[Int]
        xs.foreach(xa.add(_))
        assertTrue(xa.isEmpty)
        ys.foreach(ya.add(_))
        assertEquals(hano.util.Iter(1,2), hano.util.Iter.from(xa))
        assertEquals(hano.util.Iter(3,4,5), hano.util.Iter.from(ya))
    }

    def testTrivial2 {
        val r = new TrivialResource
        val (xs, ys) = r.splitAt(2)
        val xa = new java.util.ArrayList[Int]
        val ya = new java.util.ArrayList[Int]
        ys.foreach(ya.add(_))
        assertTrue(ya.isEmpty)
        xs.foreach(xa.add(_))
        assertEquals(hano.util.Iter(1,2), hano.util.Iter.from(xa))
        assertEquals(hano.util.Iter(3,4,5), hano.util.Iter.from(ya))
    }

    def testClosed {
        val r = new TrivialResource
        val (xs, ys) = r.splitAt(2)
        val xa = new java.util.ArrayList[Int]
        val ya = new java.util.ArrayList[Int]
        xs.take(1).foreach(xa.add(_))
        assertTrue(xa.isEmpty)
        assertFalse(r.closed)
        ys.take(2).foreach(ya.add(_))
        assertTrue(r.closed)
        assertEquals(hano.util.Iter(1), hano.util.Iter.from(xa))
        assertEquals(hano.util.Iter(3,4), hano.util.Iter.from(ya))
    }

    def testClosed2 {
        val r = new TrivialResource
        val (xs, ys) = r.splitAt(2)
        val xa = new java.util.ArrayList[Int]
        val ya = new java.util.ArrayList[Int]
        xs.take(2).foreach(xa.add(_))
        assertTrue(xa.isEmpty)
        assertFalse(r.closed)
        ys.take(1).foreach(ya.add(_))
        assertTrue(r.closed)
        assertEquals(hano.util.Iter(1,2), hano.util.Iter.from(xa))
        assertEquals(hano.util.Iter(3), hano.util.Iter.from(ya))
    }

}
