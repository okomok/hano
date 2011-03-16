

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._
import scala.collection.immutable.{Vector, IndexedSeq}
import scala.collection.JavaConversions._


class BufferedTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val t = hano.Seq(1,2,3,4)
        val out = new java.util.ArrayList[IndexedSeq[Int]]
        t.buffered(2).foreach(out.add(_))
        assertEquals(Vector(Vector(1,2),Vector(2,3),Vector(3,4)), Vector.empty ++ out)
    }

    def testTrivialNoDefault: Unit = {
        import scala.collection.mutable.ArrayBuffer
        val t = hano.Seq(1,2,3,4)
        val out = new java.util.ArrayList[ArrayBuffer[Int]]
        t.buffered(2, ArrayBuffer.newBuilder).foreach(out.add(_))
        assertEquals(Vector(ArrayBuffer(1,2),ArrayBuffer(2,3),ArrayBuffer(3,4)), Vector.empty ++ out)
    }

    def testEmpty: Unit = {
        val t = hano.Empty.of[Int]
        val out = new java.util.ArrayList[IndexedSeq[Int]]
        t.buffered(2).foreach(out.add(_))
        assertTrue(out.isEmpty)
    }

    def testOne: Unit = {
        val t = hano.Seq(5)
        val out = new java.util.ArrayList[IndexedSeq[Int]]
        t.buffered(2).foreach(out.add(_))
        assertTrue(out.isEmpty)
    }

    def testNonTrivial: Unit = {
        val t = hano.Seq(1,2,3,4,5,6)
        val out = new java.util.ArrayList[IndexedSeq[Int]]
        t.buffered(4).foreach(out.add(_))
        assertEquals(Vector(Vector(1,2,3,4),Vector(2,3,4,5),Vector(3,4,5,6)), Vector.empty ++ out)
    }

    def testWasteful: Unit = {
        val t = hano.Seq(1,2,3)
        val out = new java.util.ArrayList[IndexedSeq[Int]]
        t.buffered(1).foreach(out.add(_))
        assertEquals(Vector(Vector(1),Vector(2),Vector(3)), Vector.empty ++ out)
    }

}
