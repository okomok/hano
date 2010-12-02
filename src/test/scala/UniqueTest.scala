

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class UniqueTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val tr = hano.Seq(5,4,4,4,3,2,2,2,2,2,1)
        val out = new java.util.ArrayList[Int]
        tr.unique.foreach(out.add(_))
        assertEquals(hano.util.Vector(5,4,3,2,1), hano.util.Vector.from(out))
    }

    def testFusion: Unit = {
        val tr = hano.Seq(5,5,5,4,4,4,3,2,2,2,2,2,1)
        val out = new java.util.ArrayList[Int]
        tr.unique.unique.unique.foreach(out.add(_))
        assertEquals(hano.util.Vector(5,4,3,2,1), hano.util.Vector.from(out))
    }

    def testUnique0: Unit = {
        val tr = hano.Seq.empty.of[Int]
        val out = new java.util.ArrayList[Int]
        tr.unique.foreach(out.add(_))
        assertTrue(out.isEmpty)
    }

    def testUnique1: Unit = {
        val tr = hano.Seq(9)
        val out = new java.util.ArrayList[Int]
        tr.unique.foreach(out.add(_))
        assertEquals(hano.util.Vector(9), hano.util.Vector.from(out))
    }

    def testUnique2: Unit = {
        val tr = hano.Seq(9,9,9,9,9,9)
        val out = new java.util.ArrayList[Int]
        tr.unique.foreach(out.add(_))
        assertEquals(hano.util.Vector(9), hano.util.Vector.from(out))
    }

}
