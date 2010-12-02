

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class FlattenTest extends org.scalatest.junit.JUnit3Suite {

    def testFlatMap: Unit = {
        val tr = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        tr.flatMap{e => hano.Seq.from(0 until e)}.foreach(out.add(_))
        assertEquals(hano.util.Vector(0,0,1,0,1,2,0,1,2,3,0,1,2,3,4), hano.util.Vector.from(out))
    }

    def testUnsplit: Unit = {
        val r1 = hano.Seq(1,2)
        val r2 = hano.Seq(3,4)
        val r3 = hano.Seq(5)
        val r4 = hano.Seq(6,7,8,9)
        val rs = hano.Seq(r1,r2,r3,r4)
        val sep = hano.Seq(77,88)
        val out = new java.util.ArrayList[Int]
        rs.unsplit(sep).foreach(out.add(_))
        assertEquals(hano.util.Vector(1,2,77,88,3,4,77,88,5,77,88,6,7,8,9), hano.util.Vector.from(out))
    }

}
