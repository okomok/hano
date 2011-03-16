

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._
import scala.collection.immutable.Vector
import scala.collection.JavaConversions._


class Adjacent2Test extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val t = hano.Seq(1,2,3,4)
        val out = new java.util.ArrayList[(Int, Int)]
        t.adjacent2.foreach(out.add(_))
        assertEquals(Vector((1,2),(2,3),(3,4)), Vector.empty ++ out)
    }

    def testEmpty: Unit = {
        val t = hano.Empty.of[Int]
        val out = new java.util.ArrayList[(Int, Int)]
        t.adjacent2.foreach(out.add(_))
        assertTrue(out.isEmpty)
    }

    def testOne: Unit = {
        val t = hano.Seq(5)
        val out = new java.util.ArrayList[(Int, Int)]
        t.adjacent2.foreach(out.add(_))
        assertTrue(out.isEmpty)
    }
}
