

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import junit.framework.Assert._


class OnNthTest extends org.scalatest.junit.JUnit3Suite {

    def testHead {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a.onHead(_ => out.add(99)).foreach(e => out.add(e))
        assertEquals(hano.util.Vector(99,1,2,3,4,5), hano.util.Vector.from(out))
    }

    def testTrivial {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a.onNth(2)(_ => out.add(99)).foreach(e => out.add(e))
        assertEquals(hano.util.Vector(1,2,99,3,4,5), hano.util.Vector.from(out))
    }

}
