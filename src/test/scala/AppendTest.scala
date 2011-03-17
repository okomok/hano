

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class AppendTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val a = hano.Iter(1,2,3,4,5)
        val xs = hano.Seq(1,2,3)
        val ys = hano.Seq(4,5)
        expect(a)((xs ++ ys).toIter)
    }

    def testNotAppendedIfThrown {
        val xs = hano.Seq(1,2,3)
        val ys = hano.Seq(4,5)

        val out = new java.util.ArrayList[Int]
        var propagated = false
        object MyError extends RuntimeException
        var failed = false
        try {
            for (x <- (xs ++ ys).onExit{case hano.Exit.Failure(_) => out.add(99); case _ => failed = true}) {
                if (x == 3)
                    throw MyError
                out.add(x)
            }
        } catch {
            case MyError => propagated = true
        }
        assert(!failed)
        assert(propagated)
        expect(hano.Iter(1,2,99))(hano.Iter.from(out))
    }

}
