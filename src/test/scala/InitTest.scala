

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class InitTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val t = hano.Seq(4,5,1,3,2,9,7,10)
        val k = t.init
        assertEquals(hano.util.Vector(4,5,1,3,2,9,7), hano.util.Vector.make(k))
    }

    def testEmpty {
        val t = hano.Seq.empty
        val k = t.init
        assertTrue(hano.util.Vector.make(k).isEmpty)
    }

}
