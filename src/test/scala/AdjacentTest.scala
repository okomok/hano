

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class AdjacentTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val t = hano.Seq(1,2,3,4)
        val out = new java.util.ArrayList[scala.collection.immutable.IndexedSeq[Int]]
        t.adjacent(2).foreach(out.add(_))
        assertEquals(hano.util.Vector(scala.collection.immutable.Vector(1,2),scala.collection.immutable.Vector(2,3),scala.collection.immutable.Vector(3,4)), hano.util.Vector.from(out))
    }

    def testEmpty: Unit = {
        val t = hano.Seq.empty.of[Int]
        val out = new java.util.ArrayList[scala.collection.immutable.IndexedSeq[Int]]
        t.adjacent(2).foreach(out.add(_))
        assertTrue(out.isEmpty)
    }

    def testOne: Unit = {
        val t = hano.Seq(5)
        val out = new java.util.ArrayList[scala.collection.immutable.IndexedSeq[Int]]
        t.adjacent(2).foreach(out.add(_))
        assertTrue(out.isEmpty)
    }

    def testNonTrivial: Unit = {
        val t = hano.Seq(1,2,3,4,5,6)
        val out = new java.util.ArrayList[scala.collection.immutable.IndexedSeq[Int]]
        t.adjacent(4).foreach(out.add(_))
        assertEquals(hano.util.Vector(scala.collection.immutable.Vector(1,2,3,4),scala.collection.immutable.Vector(2,3,4,5),scala.collection.immutable.Vector(3,4,5,6)), hano.util.Vector.from(out))
    }

    def testWasteful: Unit = {
        val t = hano.Seq(1,2,3)
        val out = new java.util.ArrayList[scala.collection.immutable.IndexedSeq[Int]]
        t.adjacent(1).foreach(out.add(_))
        assertEquals(hano.util.Vector(scala.collection.immutable.Vector(1),scala.collection.immutable.Vector(2),scala.collection.immutable.Vector(3)), hano.util.Vector.from(out))
    }

}
