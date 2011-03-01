

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class EmptyTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Empty.of[Int]) {
            s.add(x)
        }
        assertTrue(s.isEmpty)
    }

    def testEnded {
        var ended = false
        var failed = false
        hano.Empty.of[Int] onExit {
            case hano.Exit.Success => ended = true
            case _ => failed = true
        } start;
        assert(!failed)
        assert(ended)
    }

    def testNoSuccess {
        var failed = false
        hano.Empty.of[Int].noSuccess onExit {
            case _ => failed = true
        } start;
        assert(!failed)
    }
}
