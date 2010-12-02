

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.madatest; package sequencetest; package reactivetest


import com.github.okomok.mada

import mada.sequence._
import junit.framework.Assert._


class HeaderTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val r1 = reactive.Of(1,2,3)
        val out = new java.util.ArrayList[Int]
        for (x <- r1 header iterative.Of(4,5)) {
            out.add(x)
        }
        assertEquals(iterative.Of(4,5,1,2,3), iterative.from(out))
    }

}
