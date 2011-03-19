

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class InitTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val t = hano.Seq(4,5,1,3,2,9,7,10)
        val k = t.init
        assertEquals(hano.Iter(4,5,1,3,2,9,7), k.toIter)
    }

    def testEmpty {
        val t = hano.Empty
        val k = t.init
        assertTrue(k.toIterable.isEmpty)
    }

}
