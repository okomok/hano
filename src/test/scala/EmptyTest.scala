

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class EmptyTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq.empty.of[Int]) {
            s.add(x)
        }
        assertTrue(s.isEmpty)
    }

    def testEnded {
        var ended = false
        hano.Seq.empty.of[Int] onExit {
            case hano.Exit.End => ended = true
            case _ => fail("doh")
        } start;
        assert(ended)
    }

    def testNever {
        hano.Seq.never.of[Int] onExit {
            case _ => fail("doh")
        } start;
        ()
    }
}
