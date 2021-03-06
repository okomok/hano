

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano
import com.github.okomok.hano.{generator, Iter}
import scala.util.continuations.suspendable
import junit.framework.Assert._


class CpsGeneratorTest extends org.scalatest.junit.JUnit3Suite {
    def makeEmpty(y: generator.cps.Env[Int]): Unit @suspendable = ()

    def testEmpty: Unit = {
        val tr = generator.cps(makeEmpty)
        assertTrue(tr.isEmpty)
        assertTrue(tr.isEmpty) // run again.
    }

    def makeValuesTo(n: Int)(y: generator.cps.Env[Int]): Unit @suspendable = {
        var i = 1
        while (i <= n) {
            y(i)
            i += 1
        }
    }

    def withMakeValuesTo(n: Int): Unit = {
        val tr = generator.cps(makeValuesTo(n))
        assertEquals(Iter.from(1 to n), Iter.from(tr))
        assertEquals(Iter.from(1 to n), Iter.from(tr)) // run again.
    }

    def testTrivial: Unit = {
        withMakeValuesTo(1)
        withMakeValuesTo(2)
        withMakeValuesTo(3)
        withMakeValuesTo(9)
        withMakeValuesTo(11)
        withMakeValuesTo(19)
        withMakeValuesTo(20)
        withMakeValuesTo(21)
        withMakeValuesTo(25)
        withMakeValuesTo(30)
        withMakeValuesTo(60)
        withMakeValuesTo(67)
        withMakeValuesTo(80)
        withMakeValuesTo(82)
        withMakeValuesTo(300)
        withMakeValuesTo(310)
    }

    def testTrivial2 {
        def example = generator.cps[Any] { `yield` =>
            `yield`("first")
            var i = 1
            while (i < 4) {
                `yield`(i)
                i += 1
            }
            `yield`("last")
        }
        for (a <- example) {
            //println(a)
        }
    }

    def testExceptionForwarding: Unit = {
        def throwSome(y: generator.cps.Env[Int]): Unit @suspendable = {
            var i = 1
            while (i <= 27) {
                y(i)
                i += 1
            }
            throw new Error("exception forwarding")
        }

        val tr = generator.cps(throwSome)

        var thrown = false
        val arr = new java.util.ArrayList[Int]

        try {
            val it = tr.iterator
            while (it.hasNext) {
                arr.add(it.next)
            }
        } catch {
            case _: Error => thrown = true
        }
        assertTrue(thrown)
        assertEquals(Iter.from(1 to 27), Iter.from(arr))
    }

    def testExceptionForwardingEmpty: Unit = {
        def throwImmediately(y: generator.cps.Env[Int]): Unit @suspendable = {
            throw new Error("exception forwarding")
        }
        val tr = generator.cps(throwImmediately)

        var thrown = false
        val arr = new java.util.ArrayList[Int]
        try {
            val it = tr.iterator
            while (it.hasNext) {
                arr.add(it.next)
            }
        } catch {
            case _: Error => thrown = true
        }
        assertTrue(thrown)
        assertTrue(arr.isEmpty)
    }

    def testAmb1 {
        val it = generator.cps[Int] { * =>
            val x = *.amb(0 until 10)
            hano.cps.require(x % 2 == 0)
            *(x)
        }
        expect(Iter(0,2,4,6,8))(Iter.from(it))
    }

    def testAmb2 {
        val it = generator.cps[(Int, Int)] { * =>
            val x = *.amb(0 until 5)
            val y = *.amb(5 until 10)
            hano.cps.require(x + y == 7)
            *((x, y))
        }
        expect(Iter((0,7),(1,6),(2,5)))(Iter.from(it))
    }

    def testAmbValueDiscarding {
        val it = generator.cps[(Int, Int)] { * =>
            val x = *.amb(0 until 5)
            val y = *.amb(5 until 10)
            hano.cps.require(x + y == 7)
            *((x, y))
            99
        }
        expect(Iter((0,7),(1,6),(2,5)))(Iter.from(it))
    }

    def testEachValueDiscarding2 {
        val it = hano.cps {
            val x = hano.from(0 until 5).!
            val y = hano.from(5 until 10).!
            (x, y)
        }
    }

    def testValueDiscarding3 {
        val it = generator.cps[Int] { * =>
            *(4)
            999
        }
    }

    def testAmbWithOneElementSeq {
        val it = generator.cps[(Int, Int, Int)] { * =>
            val x = *.amb(0 until 2)
            val y = *.get(hano.from(0 until 9).find(_ == 7))
            val z = *.amb(0 until 3)
            *((x, y, z))
        }
        expect(Iter((0,7,0),(0,7,1),(0,7,2),(1,7,0),(1,7,1),(1,7,2)))(Iter.from(it))
    }

/*
    Clearly never works.
    def testAsync {
        val it = generator.cps[Int] { y =>
            val x = hano.async.pull(0 until 20).toCps
            y(x)
        }
        Thread.sleep(2000)
        expect(Iter.from(0 until 20))(Iter.from(it))
    }
*/
}
