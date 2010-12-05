

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import junit.framework.Assert._


class DropTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val t = hano.Seq(4,5,1,3,2,9,7,10)
        val k = t.drop(5)
        assertEquals(hano.util.Iter(9,7,10), k.toIter)
        val k_ = t.drop(7)
        assertEquals(hano.util.Iter(10), k_.toIter)
        assertTrue(t.drop(8).toIter.isEmpty)
        assertTrue(t.drop(9).toIter.isEmpty)
        assertTrue(t.drop(80).toIter.isEmpty)
    }

}
