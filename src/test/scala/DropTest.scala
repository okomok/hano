

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import junit.framework.Assert._


class DropTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val t = hano.Seq(4,5,1,3,2,9,7,10)
        val k = t.drop(5)
        assertEquals(hano.util.Vector(9,7,10), hano.util.Vector.from(k))
        val k_ = t.drop(7)
        assertEquals(hano.util.Vector(10), hano.util.Vector.from(k_))
        assertTrue(hano.util.Vector.from(t.drop(8)).isEmpty)
        assertTrue(hano.util.Vector.from(t.drop(9)).isEmpty)
        assertTrue(hano.util.Vector.from(t.drop(80)).isEmpty)
    }

}
