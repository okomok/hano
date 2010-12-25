

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._
import hano.Exit


class OnceTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial {
        val out = new java.util.ArrayList[Int]
        var thrown = false

        class Trivial extends hano.Seq[Int] with hano.ForloopOnce[Int] {
            override protected def forloopOnce(f: hano.Reaction[Int]) {
                f(10)
                f(5)
            }
        }

        val r = new Trivial
        r.start
        try {
            r.start
        } catch {
            case e: hano.ForloopOnceException[_] => thrown = true
            case _ => fail("doh")
        }

        assertTrue(thrown)
    }
}
