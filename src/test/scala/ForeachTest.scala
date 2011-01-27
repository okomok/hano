

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ForeachTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val a = hano.Iter(1,6,7,10,14,17)
        val t = new java.util.ArrayList[Int]
        hano.Seq.from(a).foreach{ e => t.add(e) }
        assertEquals(a, hano.Iter.from(t))
    }
}
