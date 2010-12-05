

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ZipTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val t = hano.Seq(1,2,3)
        val u = hano.Seq("2","3","4")
        val out = new java.util.ArrayList[(Int, String)]
        t.zip(u).foreach(out.add(_))
        assertEquals(hano.util.Iter((1,"2"),(2,"3"),(3,"4")), hano.util.Iter.from(out))
    }

    def testEmpty1: Unit = {
        val t = hano.Seq.empty.of[Int]
        val u = hano.Seq("2","3","4")
        val out = new java.util.ArrayList[(Int, String)]
        t.zip(u).foreach(out.add(_))
        assertTrue(out.isEmpty)
    }

    def testEmpty2: Unit = {
        val t = hano.Seq.empty.of[Int]
        val u = hano.Seq.empty.of[String]
        val out = new java.util.ArrayList[(Int, String)]
        t.zip(u).foreach(out.add(_))
        assertTrue(out.isEmpty)
    }

    def testOneShorter1: Unit = {
        val t = hano.Seq(1,2)
        val u = hano.Seq("2","3","4")
        val out = new java.util.ArrayList[(Int, String)]
        t.zip(u).foreach(out.add(_))
        assertEquals(hano.util.Iter((1,"2"),(2,"3")), hano.util.Iter.from(out))
    }

    def testOneShorter2: Unit = {
        val t = hano.Seq(1,2,3)
        val u = hano.Seq("2","3")
        val out = new java.util.ArrayList[(Int, String)]
        t.zip(u).foreach(out.add(_))
        assertEquals(hano.util.Iter((1,"2"),(2,"3")), hano.util.Iter.from(out))
    }

    def testMuchShorter1: Unit = {
        val t = hano.Seq(1,2)
        val u = hano.Seq("2","3","4","5","6")
        val out = new java.util.ArrayList[(Int, String)]
        t.zip(u).foreach(out.add(_))
        assertEquals(hano.util.Iter((1,"2"),(2,"3")), hano.util.Iter.from(out))
    }

    def testMuchShorter2: Unit = {
        val t = hano.Seq(1,2,3,4,5,6)
        val u = hano.Seq("2","3")
        val out = new java.util.ArrayList[(Int, String)]
        t.zip(u).foreach(out.add(_))
        assertEquals(hano.util.Iter((1,"2"),(2,"3")), hano.util.Iter.from(out))
    }

    def testParallel: Unit = {
        //for (_ <- 0 to 30) {
            val src = new IntSenders(hano.util.Iter(1,2,3,4,5,6), hano.util.Iter(7,7,7,7))
            val dst = new IntReceiver(hano.util.Iter(8,9,10,11))
            (src(0) zip src(1)).map{case (x, y) => x + y}.foreach(dst)
            src.activate
            src.shutdown(dst.assertMe)
        //}
    }

}
