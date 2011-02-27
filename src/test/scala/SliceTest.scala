

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class SliceTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val t = hano.Seq(1,2,3,4,5,6,7,8)
        val s = new java.util.ArrayList[Int]
        t.slice(1, 5).onExit(_ =>s.add(99)).foreach(s.add(_))
        assertEquals(hano.Iter(2,3,4,5,99), hano.Iter.from(s))
    }
}
