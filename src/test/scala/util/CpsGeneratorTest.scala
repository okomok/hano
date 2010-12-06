

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package utiltest


import com.github.okomok.hano
import com.github.okomok.hano.util.{CpsGenerator, Iter}
import scala.util.continuations.suspendable
import junit.framework.Assert._


class CpsGeneratorTest extends org.scalatest.junit.JUnit3Suite {
    def makeEmpty(y: CpsGenerator.Env[Int]): Unit @suspendable = ()

    def testEmpty: Unit = {
        val tr = CpsGenerator(makeEmpty)
        assertTrue(tr.isEmpty)
        assertTrue(tr.isEmpty) // run again.
    }

    def makeValuesTo(n: Int)(y: CpsGenerator.Env[Int]): Unit @suspendable = {
        var i = 1
        while (i <= n) {
            y(i)
            i += 1
        }
    }

    def withMakeValuesTo(n: Int): Unit = {
        val tr = CpsGenerator(makeValuesTo(n))
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
        def example = CpsGenerator[Any] { `yield` =>
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
        def throwSome(y: CpsGenerator.Env[Int]): Unit @suspendable = {
            var i = 1
            while (i <= 27) {
                y(i)
                i += 1
            }
            throw new Error("exception forwarding")
        }

        val tr = CpsGenerator(throwSome)

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
        assertEquals(Iter.from(1 to 26), Iter.from(arr)) // iterator-from-cursor prefetches one-element.
    }

    def testExceptionForwardingEmpty: Unit = {
        def throwImmediately(y: CpsGenerator.Env[Int]): Unit @suspendable = {
            throw new Error("exception forwarding")
        }
        val tr = CpsGenerator(throwImmediately)

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
        val it = CpsGenerator[Int] { * =>
            val x = *.amb(0 until 10)
            *.require(x % 2 == 0)
            *(x)
        }
        expect(Iter(0,2,4,6,8))(Iter.from(it))
    }

    def testAmb2 {
        val it = CpsGenerator[(Int, Int)] { * =>
            val x = *.amb(0 until 5)
            val y = *.amb(5 until 10)
            *.require(x + y == 7)
            *((x, y))
        }
        expect(Iter((0,7),(1,6),(2,5)))(Iter.from(it))
    }

    def testAmbValueDiscarding {
        val it = CpsGenerator[(Int, Int)] { * =>
            val x = *.amb(0 until 5)
            val y = *.amb(5 until 10)
            *.require(x + y == 7)
            *((x, y))
            99
        }
        expect(Iter((0,7),(1,6),(2,5)))(Iter.from(it))
    }

    def testEachValueDiscarding2 {
        val it = hano.Block { * =>
            val x = *.each(0 until 5)
            val y = *.each(5 until 10)
            (x, y)
        }
    }

    def testValueDiscarding3 {
        val it = CpsGenerator[Int] { * =>
            *(4)
            999
        }
    }

    def testAmbWithOneElementSeq {
        val it = CpsGenerator[(Int, Int, Int)] { * =>
            val x = *.amb(0 until 2)
            val y = *.find(0 until 9)(_ == 7)
            val z = *.amb(0 until 3)
            *((x, y, z))
        }
        expect(Iter((0,7,0),(0,7,1),(0,7,2),(1,7,0),(1,7,1),(1,7,2)))(Iter.from(it))
    }

/*
    Clearly never works.
    def testAsync {
        val it = CpsGenerator[Int] { y =>
            val x = hano.Seq.origin(hano.eval.Async).generate(0 until 20).toCps
            y(x)
        }
        Thread.sleep(2000)
        expect(Iter.from(0 until 20))(Iter.from(it))
    }
*/
}