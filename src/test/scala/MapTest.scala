

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class MapTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val s = new java.util.ArrayList[Int]
        hano.Seq("123", "12", "12345").map(_.length).foreach(s.add(_))
        assertEquals(hano.Iter(3,2,5), hano.Iter.from(s))
    }

    def testEmpty: Unit = {
        val s = new java.util.ArrayList[Int]
        hano.Empty.of[String].map(_.length).foreach(s.add(_))
        assertTrue(s.isEmpty)
    }
}
