

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class GenerateTest extends org.scalatest.junit.JUnit3Suite {

    def testLonger: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(hano.Iter(9,8,7,6,5,4,3))) {
            s.add(x)
        }
        assertEquals(hano.Iter(9,8,7,6,5), hano.Iter.from(s))
    }

    def testShorter: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(hano.Iter(9,8))) {
            s.add(x)
        }
        assertEquals(hano.Iter(9,8), hano.Iter.from(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(hano.Iter().of[Int])) {
            s.add(x)
        }
        assertTrue(s.isEmpty)
    }

    def testInfinite: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(Stream.continually(9))) {
            s.add(x)
        }
        assertEquals(hano.Iter(9,9,9,9,9), hano.Iter.from(s))
    }

    def testThen: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Context.strict.generate(hano.Iter(9,8,7,6,5)).onExit(_ =>s.add(99))) {
            s.add(x)
        }
        assertEquals(hano.Iter(9,8,7,6,5,99), hano.Iter.from(s))
    }

    def testThenAppend: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Context.strict.generate(hano.Iter(9,8,7,6,5)) ++ hano.Seq(2,3,4)) {
            s.add(x)
        }
        assertEquals(hano.Iter(9,8,7,6,5,2,3,4), hano.Iter.from(s))
    }

}
