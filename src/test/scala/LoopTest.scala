

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class LoopTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val xs = hano.Self.loop.generate(1 until 4)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2))(xs.loop.take(11).toIter)
    }

    def testTrivial2 {
        val xs = hano.Act().loop.generate(1 until 4)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2))(xs.loop.take(11).toIter)
    }

    def testClose {
        val xs = hano.Act().loop.generate(1 until 100)

        val out = new java.util.ArrayList[Int]
        var i = 0
        val ys = xs.loop
        ys onEach { x =>
            out add i
            i += 1
            if (i == 50) {
                ys.close()
            }
        } await()

        expect(hano.Iter.from(0 until 50))(hano.Iter.from(out))
    }

    def testCloseIndirect {
        val xs = hano.Act().loop.generate(1 until 100)

        val out = new java.util.ArrayList[Int]
        var i = 0
        xs.loop onEach { x =>
            out add i
            i += 1
            if (i == 50) {
                xs.close()
            }
        } await()

        expect(hano.Iter.from(0 until 50))(hano.Iter.from(out))
    }

    def testCloseParallel {
        val suite = new ParallelSuite(10)
        val xs = hano.Act().loop.generate(1 until 100)
        suite.add(1) {
            for (x <- xs.loop.take(500)) {
                ()
            }
        }
        suite.add(100) {
            Thread.sleep(100)
            xs.close
        }
        suite.start()
        Thread.sleep(1500)
    }
}