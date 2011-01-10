

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class BoundedSetTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val rx = new hano.BoundedSet[Int](5)
        hano.Context.act.eval {
            rx offer 12
            rx offer 13
            rx offer 5
            rx offer 4
            rx offer 6
        }
        expect(hano.Iter(12,13,5,4,6))(rx.toIter)
    }

    def testOffer {
        val xs = new hano.BoundedSet[Int](2)
        expect(true)(xs offer 2)
        expect(true)(xs offer 3)
        expect(false)(xs offer 4)
    }

    def testEmpty {
        val xs = new hano.BoundedSet[Int](0)
        expect(false)(xs offer 2)
    }

    def testSignal {
        val ctx = hano.Context.act
        val a = new hano.BoundedSet[Int](2, ctx)
        val b = new hano.BoundedSet[Int](2, ctx)
        val ys = for (x <- a; y <- b) yield (x + y)
        hano.Context.act.eval {
            a offer 1
            b offer 2
            a offer 7
            b offer 35
        }

        // Note elements order is indeterminate in case pallalel offer/subscription.
        expect(hano.Iter(3,36,9,42))(ys.take(4).toIter) // take is needed; flatMap has no end.
    }

    def testParallel {
        val suite = new ParallelSuite(10)
        val xs = new hano.BoundedSet[Int](100)
        suite.add(100) {
            xs offer 10
        }
        suite.add(100) {
            xs offer 12
        }
        suite.start

        val it = xs.toIterable
        expect(100)(it.size)
        assert(it.forall(x => x == 10 || x == 12))
    }
}
