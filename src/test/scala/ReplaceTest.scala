

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ReplaceTest extends org.scalatest.junit.JUnit3Suite {

    def testLonger: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).replace(hano.util.Vector(9,8,7,6,5,4,3))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,8,7,6,5), hano.util.Vector.make(s))
    }

    def testShorter: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).replace(hano.util.Vector(9,8))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,8,2,3,4), hano.util.Vector.make(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).replace(hano.util.Vector.emptyOf[Int])) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(0,1,2,3,4), hano.util.Vector.make(s))
    }

    def testInfinite: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).generate(Stream.continually(9))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(9,9,9,9,9), hano.util.Vector.make(s))
    }

    def testRegion1: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).replaceRegion(2,4,hano.util.Vector(9,8,7,6,5,4,3))) {
            s.add(x)
        }
        assertEquals(hano.util.Vector(0,1,9,8,4), hano.util.Vector.make(s))
    }

}
