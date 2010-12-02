

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class OfTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a.foreach(e => out.add(e))
        assertEquals(hano.util.Iterable(1,2,3,4,5), hano.util.Iterable.from(out))
    }
}
