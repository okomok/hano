

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import junit.framework.Assert._


class OnNthTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a.onNth(2){case Some(_) => out.add(99); case None => ()}.foreach(e => out.add(e))
        assertEquals(hano.Iter(1,2,99,3,4,5), hano.Iter.from(out))
    }

    def testHead {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a.onHead{case Some(x) => out.add(x*10); case None => ()}.foreach(e => out.add(e))
        assertEquals(hano.Iter(10,1,2,3,4,5), hano.Iter.from(out))
    }

    def testHeadEmpty {
        val a = hano.Seq[Int]()
        val out = new java.util.ArrayList[Int]
        a.onHead{case Some(x) => out.add(x*10); case None => out.add(99)}.foreach(e => out.add(e))
        assertEquals(hano.Iter(99), hano.Iter.from(out))
    }

    def testLast {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a.onLast{case Some(x) => out.add(x*10); case None => ()}.foreach(e => out.add(e))
        assertEquals(hano.Iter(1,2,3,4,5,50), hano.Iter.from(out))
    }

    def testLastEmpty {
        val a = hano.Seq[Int]()
        val out = new java.util.ArrayList[Int]
        a.onLast{case Some(x) => out.add(x*10); case None => out.add(99)}.foreach(e => out.add(e))
        assertEquals(hano.Iter(99), hano.Iter.from(out))
    }
}
