

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
        a.onHead{case Some(x) => out.add(x); case None => ()}.start
        assertEquals(hano.Iter(1), hano.Iter.from(out))
    }

    def testHeadEmpty {
        val a = hano.Seq()
        val out = new java.util.ArrayList[Int]
        a.onHead{case Some(_) => out.add(99); case None => out.add(100)}.start
        assertEquals(hano.Iter(100), hano.Iter.from(out))
    }

    def testLast {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a.onLast{case Some(x) => out.add(x); case None => ()}.start
        assertEquals(hano.Iter(5), hano.Iter.from(out))
    }

    def testLastEmpty {
        val a = hano.Seq()
        val out = new java.util.ArrayList[Int]
        a.onLast{case Some(_) => out.add(99); case None => out.add(100)}.start
        assertEquals(hano.Iter(100), hano.Iter.from(out))
    }
}
