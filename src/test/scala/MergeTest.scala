

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class MergeTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val r1 = hano.Seq(1,2,3)
        val r2 = hano.Seq(4,5)
        val out = new java.util.ArrayList[Int]
        for (x <- r1 merge r2) {
            out.add(x)
        }
        assertEquals(hano.util.Vector(1,2,3,4,5), hano.util.Vector.from(out))
    }

    def testNonTrivial: Unit = {
        val r1 = hano.Seq(1,2,3)
        val r2 = hano.Seq(4,5)
        val r3 = hano.Seq(6,7,8,9)
        val out = new java.util.ArrayList[Int]
        for (x <- r1 merge r2 merge r3) {
            out.add(x)
        }
        assertEquals(hano.util.Vector(1,2,3,4,5,6,7,8,9), hano.util.Vector.from(out))
    }

    def testDuplicate {
        val r = hano.Seq(1,2,3,4,5)
        val (r1, r2) = r.duplicate
        val out = new java.util.ArrayList[Int]
        for (x <- r1 merge r2) {
            out.add(x)
        }
        assertEquals(hano.util.Vector(1,1,2,2,3,3,4,4,5,5), hano.util.Vector.from(out))
    }

}
