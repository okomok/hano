

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class TakeWhileTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial0: Unit = {
        val a = hano.util.Vector(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).takeWhile(_ <= 4).foreach(b.add(_))
        assertEquals(hano.util.Vector(1,2,3,4), hano.util.Vector.from(b))
    }

    def testAll: Unit = {
        val a = hano.util.Vector(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).takeWhile(_ <= 10).foreach(b.add(_))
        assertEquals(hano.util.Vector(1,2,3,4,5,6), hano.util.Vector.from(b))
    }

    def testEmpty: Unit = {
        val a = hano.util.Vector.emptyOf[Int]
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).takeWhile(_ <= 10).foreach(b.add(_))
        assertTrue(b.isEmpty)
    }

    def testNone: Unit = {
        val a = hano.util.Vector(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).takeWhile(_ > 10).foreach(b.add(_))
        assertTrue(b.isEmpty)
    }

    def testThen: Unit = {
        val a = hano.util.Vector(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).takeWhile(_ <= 4).onExit(_ =>b.add(99)).foreach(b.add(_))
        assertEquals(hano.util.Vector(1,2,3,4,99), hano.util.Vector.from(b))
    }

    def testThen2: Unit = {
        val a = hano.util.Vector(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).takeWhile(_ <= 4).onExit(_ =>b.add(98)).onExit(_ =>b.add(99)).foreach(b.add(_))
        assertEquals(hano.util.Vector(1,2,3,4,98,99), hano.util.Vector.from(b))
    }

    def testThenAppend: Unit = {
        val a = hano.util.Vector(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        (hano.Seq.from(a).takeWhile(_ <= 4) ++ hano.Seq(5,6,7)).onExit(_ =>b.add(99)).foreach(b.add(_))
        assertEquals(hano.util.Vector(1,2,3,4,5,6,7,99), hano.util.Vector.from(b))
    }
}
