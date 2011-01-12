

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class SetTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val rx = new hano.Set[Int](5)
        expect(5)(rx.size)
        expect(false)(rx.isEmpty)
        hano.Context.act.eval {
            rx member 12
            rx member 13
            rx += 5
            rx += 4
            rx member 6
        }
        expect(hano.Iter(12,13,5,4,6))(rx.take(5).toIter)
    }

    def testOffer {
        val xs = new hano.Set[Int](2)
        expect(true)(xs += 2)
        expect(true)(xs += 3)
        expect(false)(xs member 4)
    }

    def testEmpty {
        val xs = new hano.Set[Int](0)
        expect(false)(xs member 2)
        expect(true)(xs.isEmpty)
        expect(0)(xs.size)
    }

    def testSignal {
        val ctx = hano.Context.act
        val a = new hano.Set[Int](2, ctx)
        val b = new hano.Set[Int](2, ctx)
        val ys = for (x <- a; y <- b) yield (x + y)
        hano.Context.act.eval {
            a member 1
            b member 2
            a member 7
            b member 35
        }

        // Note elements order is indeterminate in case pallalel member/subscription.
        expect(hano.Iter(3,36,9,42))(ys.take(4).toIter) // take is needed; flatMap has no end.
    }

    def testParallel {
        val suite = new ParallelSuite(10)
        val xs = new hano.Set[Int](100)
        suite.add(100) {
            xs member 10
        }
        suite.add(100) {
            xs member 12
        }
        suite.start

        val it = xs.take(100).toIterable
        assert(it.forall(x => x == 10 || x == 12))
    }
}
