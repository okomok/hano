

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class CycleTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val xs = hano.Self.pull(1 until 4)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2))(xs.cycle.take(11).toIter)
    }

    def testTrivial2 {
        for (i <- 0 until 300) {
            val xs = hano.async.pull(1 until 4)
            expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2))(xs.cycle.take(11).toIter)
        }
    }

    /*
    def testClosingShallBeSync {
        val xs = hano.async.closing{Thread.sleep(100);false}.pull(1 until 4).closing{Thread.sleep(100);false}
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2))(xs.cycle.take(11).toIter)
    }
    */

    def testReact {
        val xs = hano.async.onEach(_ => ()).pull(1 until 4)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2))(xs.cycle.take(11).toIter)
    }

    def testBreak {
        val xs = hano.async.pull(1 until 100)

        val out = new java.util.ArrayList[Int]
        var i = 0
        val ys = xs.cycle
        ys onEach { x =>
            out add i
            i += 1
            if (i == 50) {
                hano.break
            }
        } await()

        expect(hano.Iter.from(0 until 50))(hano.Iter.from(out))
    }

    def testCloseIndirect {
        val xs = hano.async.pull(1 until 100)

        val out = new java.util.ArrayList[Int]
        var i = 0
        xs.cycle onEach { x =>
            out add i
            i += 1
            if (i == 50) {
                hano.break
            }
        } await()

        expect(hano.Iter.from(0 until 50))(hano.Iter.from(out))
    }

/*
    def testCloseParallel {
        val suite = new ParallelSuite(10)
        val xs = hano.async.pull(1 until 100)
        suite.add(1) {
            for (x <- xs.cycle.take(500)) {
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
    def testResource {
        class MyResource extends hano.SeqResource[Int] {
            override val process = hano.Self
            override def closeResource = ()
            override def openResource(f: hano.Reaction[Int]) {
                f(1); f(2); f(3); f.exit()
            }
        }
        val xs = new MyResource().take(3).cycle // take(3) guarantees `close`.
        expect(hano.Iter(1,2,3,1,2,3,1,2))(xs.take(8).toIter)
    }

    def testResource2 {
        var count = 0
        class MyResource extends hano.Seq[Int] {
            count += 1
            override val process = hano.Self
            override def forloop(f: hano.Reaction[Int]) {
                f(1); f(2); f(3); f.exit()
            }
        }
        val xs = hano.byName(new MyResource).cycle
        expect(hano.Iter(1,2,3,1,2,3,1,2))(xs.take(8).toIter)
        expect(3)(count)
    }
*/
}
