

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class AdaptTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val r = hano.Seq(1,2,3,4,5,6)
        val out = new java.util.ArrayList[Int]
        r.
            fork{r => r.onEach(e => out.add(e *  2))}.
            fork{r => r}.
            adapt { (xs, _) =>  xs.start() }.
            fork{r => r.onEach(e => out.add(e + 10))}.
            fork{r => r}.
            start

        assertEquals(hano.Iter(2,4,6,8,10,12), hano.Iter.from(out))
    }
}
