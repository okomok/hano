

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class TailTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val s = new java.util.ArrayList[Int]
        hano.Seq(0,1,2,3,4).tail.foreach(s.add(_))
        assertEquals(hano.Iter(1,2,3,4), hano.Iter.from(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        hano.Empty.of[Int].tail.foreach(s.add(_))
        assertTrue(s.isEmpty)
    }
}
