

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class GenerateTest extends org.scalatest.junit.JUnit3Suite {

    def testLonger: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(hano.util.Vector(9,8,7,6,5,4,3))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,8,7,6,5), hano.util.Vector.from(s))
    }

    def testShorter: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(hano.util.Vector(9,8))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,8), hano.util.Vector.from(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(hano.util.Iterable.emptyOf[Int])) {
            s.add(x)
        }
        assertTrue(s.isEmpty)
    }

    def testInfinite: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(hano.util.Iterable.repeat(9))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,9,9,9,9), hano.util.Vector.from(s))
    }

    def testThen: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq.origin(hano.eval.Strict).generate(hano.util.Vector(9,8,7,6,5)).onExit(_ =>s.add(99))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,8,7,6,5,99), hano.util.Vector.from(s))
    }

    def testThenAppend: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq.origin(hano.eval.Strict).generate(hano.util.Vector(9,8,7,6,5)) ++ hano.Seq(2,3,4)) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,8,7,6,5,2,3,4), hano.util.Vector.from(s))
    }

}
