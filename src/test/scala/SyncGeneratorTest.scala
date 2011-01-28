

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano
import junit.framework.Assert._

import hano.Generator


class SyncGeneratorTest extends org.scalatest.junit.JUnit3Suite {


    def testEmpty: Unit = {
        val tr = Generator.Sync[Int] { * =>
            999
            *.end()
        }
        assertTrue(tr.isEmpty)
        assertTrue(tr.isEmpty) // run again.
    }

    def makeValuesTo(n: Int)(y: Generator.Sync.Env[Int]): Unit = {
        for (i <- 1 to n) {
            y(i)
        }
        y.end()
    }

    def withMakeValuesTo(n: Int): Unit = {
        val tr = Generator.Sync(makeValuesTo(n))
        assertEquals(hano.Iter.from(1 to n), hano.Iter.from(tr))
        assertEquals(hano.Iter.from(1 to n), hano.Iter.from(tr)) // run again.
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
        def example =  Generator.Sync[Any] { * =>
            *("first")
            for (i <- 1 until 4) {
                *(i)
            }
            *("last")
            *.end()
//            *.end() // never idempotent
        }
        for (a <- example) {
            //println(a)
        }
    }

    def testExceptionForwarding: Unit = {
        def throwSome(y: Generator.Sync.Env[Int]): Unit = {
            for (i <- 1 to 27) {
                y(i)
            }
            throw new Error("exception forwarding")
        }

        val tr = Generator.Sync(throwSome)

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
        assertEquals(hano.Iter.from(1 to 27), hano.Iter.from(arr))
    }

    def testExceptionForwardingEmpty: Unit = {
        def throwImmediately(y: Generator.Sync.Env[Int]) {
            throw new Error("exception forwarding")
        }
        val tr = Generator.Sync(throwImmediately)

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

    def testFlush {
        def sample = Generator.Sync[Int] { y =>
            for (i <- 0 until 20) {
                y(i)
            } // exchange.
            y(20)
            y(21)
            y(22)
            y(23)
            y(24)
            y.flush() // exchange.
            throw new Error("after flush")
//            Thread.sleep(10000)
            y.end()
        }
        val ret = new java.util.ArrayList[Int]
        val it = sample.iterator
        for (_ <- 0 until 24) { // cf. Cursor will prefetch one element.
            val e = it.next
            ret.add(e)
        }
        assertEquals(hano.Iter.from(0 until 24), hano.Iter.from(ret))
    }

    def testToIterable {
        val sample = hano.Act().loop.generate(0 until 20).toIterable
        assertEquals(hano.Iter.from(0 until 20), hano.Iter.from(sample))
    }
/*
    def testTraverse {
        val sample = Generator.Sync.traverse(0 until 20)
        assertEquals(hano.Iter.from(0 until 20), hano.Iter.from(sample))
    }
*/
    def testThrowAfterEnd {
        val sample = Generator.Sync[Int] { * =>
            *(1)
            *(2)
            *(3)
            *.end()
            throw new Error("after end")
        }
        val ret = new java.util.ArrayList[Int]
        sample.foreach(ret.add(_))
        expect(hano.Iter(1,2,3))(hano.Iter.from(ret))
    }
}

/*
class GeneratorLockCompile extends Benchmark {
    val b = new GeneratorTest
    val tr = iterative.generator(b.makeValuesTo(100000))
    override def run = {
        val a = tr.size
        ()
    }
}
*/
