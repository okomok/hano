

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class IterativeTest extends org.scalatest.junit.JUnit3Suite {
    def testTo: Unit = {
        val r = hano.Seq(1,2,3,4,5,6)
        assertEquals(hano.util.Vector(1,2,3,4,5,6), hano.util.Vector.make(r))
    }

    def testLong: Unit = {
        val r = hano.Seq.from(0 until 400)
        assertEquals(0 until 400, hano.util.Vector.make(r))
    }
}
