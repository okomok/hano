

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

        assertEquals(hano.Iter(14,15,16,17,88,19), hano.Iter.from(out))
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

    def testRethrow {
        val xs = hano.async.loop.pull(0 until 10)

        object MyError1 extends RuntimeException
        object MyError2 extends RuntimeException

        var ok: Option[Boolean] = None
        xs catching {
            case MyError2 => ok = Some(true)
            case _ => ok = Some(false)
        } catching {
            case MyError1 => throw MyError2 // rethrow
            case _ => ok = Some(false)
        } onEach { x =>
            if (x == 5) {
                throw MyError1
            }
        } await()

        expect(false)(ok.isEmpty)
        expect(true)(ok.get)
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
        assertEquals(hano.Iter(14,15,16,17,88,19), hano.Iter.from(out))
    }
*/
}
