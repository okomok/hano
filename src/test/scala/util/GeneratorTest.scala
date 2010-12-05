

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package utiltest


import com.github.okomok.hano
import com.github.okomok.hano.util
import junit.framework.Assert._


class GeneratorTest extends org.scalatest.junit.JUnit3Suite {


    def testEmpty: Unit = {
        val tr = util.Generator[Int] { * =>
            999
            *.exit()
        }
        assertTrue(tr.isEmpty)
        assertTrue(tr.isEmpty) // run again.
    }

    def makeValuesTo(n: Int)(y: util.Generator.Env[Int]): Unit = {
        for (i <- 1 to n) {
            y(i)
        }
        y.exit()
    }

    def withMakeValuesTo(n: Int): Unit = {
        val tr = util.Generator(makeValuesTo(n))
        assertEquals(util.Iter.from(1 to n), util.Iter.from(tr))
        assertEquals(util.Iter.from(1 to n), util.Iter.from(tr)) // run again.
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
        def example =  util.Generator[Any] { * =>
            *("first")
            for (i <- 1 until 4) {
                *(i)
            }
            *("last")
            *.exit()
        }
        for (a <- example) {
            //println(a)
        }
    }
/*
    def testExceptionForwarding: Unit = {
        def throwSome(y: iterative.Yield[Int]): Unit = {
            for (i <- 1 to 27) {
                y(i)
            }
            throw new Error("exception forwarding")
        }

        val tr = iterative.generator(throwSome)

        var thrown = false
        val arr = new java.util.ArrayList[Int]

        try {
            val it = tr.begin
            while (it) {
                arr.add(~it)
                it.++
            }
        } catch {
            case _: Error => thrown = true
        }
        assertTrue(thrown)
        assertEquals(iterative.from(1 to 27), iterative.from(arr))
    }

    def testExceptionForwardingEmpty: Unit = {
        def throwImmediately(y: iterative.Yield[Int]) {
            throw new Error("exception forwarding")
        }
        val tr = iterative.generator(throwImmediately)

        var thrown = false
        val arr = new java.util.ArrayList[Int]
        try {
            val it = tr.begin
            while (it) {
                arr.add(~it)
                it.++
            }
        } catch {
            case _: Error => thrown = true
        }
        assertTrue(thrown)
        assertTrue(arr.isEmpty)
    }
*/
    def testFlush {
        def sample = util.Generator[Int] { y =>
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
            y.exit()
        }
        val ret = new java.util.ArrayList[Int]
        val it = sample.iterator
        for (_ <- 0 until 24) { // cf. Cursor will prefetch one element.
            val e = it.next
            ret.add(e)
        }
        assertEquals(util.Iter.from(0 until 24), util.Iter.from(ret))
    }

    def testToIterable {
        val sample = hano.Seq.origin(hano.eval.Async).generate(0 until 20).toIterable
        assertEquals(util.Iter.from(0 until 20), util.Iter.from(sample))
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
