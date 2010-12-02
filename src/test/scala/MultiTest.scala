

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class MultiTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val out = new java.util.ArrayList[Int]
        val rs = new java.util.ArrayList[Int => Unit]
        rs.add(x => out.add(x+10))
        rs.add(x => out.add(x+20))
        val t = hano.Seq(4,5,1,3)
        val k_ = t.foreach(hano.multi(rs))
        assertEquals(hano.util.Iterable(14,24,15,25,11,21,13,23), hano.util.Iterable.from(out))
    }

    def testReactive {
        val out = new java.util.ArrayList[Int]
        val rs = new java.util.ArrayList[Int => Unit]
        rs.add(x => out.add(x+10))
        rs.add(x => out.add(x+20))
        val t = hano.Seq(4,5,1,3)
        val k_ = t.foreach(hano.multi(hano.Seq.from(hano.util.Iterable.from(rs))))
        assertEquals(hano.util.Iterable(14,24,15,25,11,21,13,23), hano.util.Iterable.from(out))
    }

}
