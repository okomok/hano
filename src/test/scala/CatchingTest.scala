

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class CatchingTest extends org.scalatest.junit.JUnit3Suite {

    def testCatch: Unit = {
        val t = hano.Seq(1,2,3,4,5,6,7,8,9)

        val out = new java.util.ArrayList[Int]

        t.filter {
            _ > 3
        } catching {
            case x: AssertionError => out.add(88)
        } map { e =>
            if (e == 8) {
                throw new AssertionError
            } else {
                e + 10
            }
        } foreach { e =>
            out.add(e)
        }

        assertEquals(hano.util.Iter(14,15,16,17,88,19), hano.util.Iter.from(out))
    }

    def testThrough: Unit = {
        val t = hano.Seq(1,2,3,4,5,6,7,8,9)

        val out = new java.util.ArrayList[Int]

        class MyError extends Error

        var thrown = false
        try {
            t.filter {
                _ > 3
            } catching {
                case x: MyError => out.add(88)
            } map { e =>
                if (e == 8) {
                    throw new AssertionError
                } else {
                    e + 10
                }
            } foreach { e =>
                out.add(e)
            }
        } catch {
            case x: AssertionError => thrown = true
        }

        assertTrue(thrown)
    }

/*
    def testTrivial: Unit = {
        val t = hano.Seq(1,2,3,4,5,6,7,8,9)

        var finalOk = false

        val out = new java.util.ArrayList[Int]

        hano.`try` {
            t.
            filter { _ > 3 }
        } `catch` {
            case x: AssertionError => out.add(88)
        } `finally` {
            finalOk = true
        } map { e =>
            if (e == 8) {
                throw new AssertionError
            } else {
                e + 10
            }
        } foreach { e =>
            out.add(e)
        }

        assertTrue(finalOk)
        assertEquals(hano.util.Iter(14,15,16,17,88,19), hano.util.Iter.from(out))
    }
*/
}
