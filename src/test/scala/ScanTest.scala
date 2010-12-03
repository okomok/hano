

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ScanLeftTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val t = hano.Seq(1,2,3,4,5,6,7,8)
        val u = hano.util.Vector(5,6,8,11,15,20,26,33,41)
        val s = new java.util.ArrayList[Int]
        t.scanLeft(5)(_ + _).foreach(s.add(_))
        assertEquals(u, hano.util.Vector.make(s))
    }

    def testTrivial2: Unit = {
        val t = hano.Seq(1)
        val u = hano.util.Vector(5,6)
        val s = new java.util.ArrayList[Int]
        t.scanLeft(5)(_ + _).foreach(s.add(_))
        assertEquals(u, hano.util.Vector.make(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        hano.Seq.empty.of[Int].scanLeft(0)(_ + _).foreach(s.add(_))
        assertEquals(hano.util.Vector(0), hano.util.Vector.make(s))
    }

/*
    def testScanLeft1: Unit = {
        val t = hano.Seq(5,1,2,3,4,5,6,7,8)
        val u = hano.util.Vector(5,6,8,11,15,20,26,33,41, 99)
        val s = new java.util.ArrayList[Int]
        t.scanLeft1(_ + _).activate(reactor.make(_ => s.add(99), s.add(_)))
        assertEquals(u, hano.util.Vector.make(s))
    }

    def testScanLeft1Empty: Unit = {
        val t = hano.Seq(5,1)
        val u = hano.util.Vector(5,6, 99)
        val s = new java.util.ArrayList[Int]
        t.scanLeft1(_ + _).activate(reactor.make(_ => s.add(99), s.add(_)))
        assertEquals(u, hano.util.Vector.make(s))
    }

    def testScan1Empty: Unit = {
        val s = new java.util.ArrayList[Int]
        hano.Seq.empty.of[Int].scanLeft1(_ + _).activate(reactor.make(_ => s.add(99), s.add(_)))
        assertEquals(hano.util.Vector(99), hano.util.Vector.make(s))
    }
*/
}

class ScanLeft1Test extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val t = hano.Seq(5,1,2, 3, 4, 5, 6, 7, 8)
        val u = hano.util.Vector(5,6,8,11,15,20,26,33,41)
        val s = new java.util.ArrayList[Int]
        t.scanLeft1(_ + _).foreach(s.add(_))
        assertEquals(u, hano.util.Vector.make(s))
    }

    def testTrivial2: Unit = {
        val t = hano.Seq(5,1)
        val u = hano.util.Vector(5,6)
        val s = new java.util.ArrayList[Int]
        t.scanLeft1(_ + _).foreach(s.add(_))
        assertEquals(u, hano.util.Vector.make(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        hano.Seq.empty.of[Int].scanLeft1(_ + _).foreach(s.add(_))
        assertTrue(hano.util.Vector.make(s).isEmpty)
    }
}

