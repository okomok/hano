

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class AppendTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val a = hano.util.Iter(1,2,3,4,5)
        val xs = hano.Seq(1,2,3)
        val ys = hano.Seq(4,5)
        expect(hano.util.Iter(1,2,3,4,5))((xs ++ ys).toIter)
    }

    def testNotAppendedIfThrown {
        val xs = hano.Seq(1,2,3)
        val ys = hano.Seq(4,5)

        val out = new java.util.ArrayList[Int]
        var propagated = false
        object MyError extends Error
        try {
            for (x <- (xs ++ ys).onExit{case hano.Exit.Thrown(_) => out.add(99); case _ => fail("doh")}) {
                if (x == 3)
                    throw MyError
                out.add(x)
            }
        } catch {
            case MyError => propagated = true
        }
        assert(propagated)
        expect(hano.util.Iter(1,2,99))(hano.util.Iter.from(out))
    }

}
