

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class SyncTest extends org.scalatest.junit.JUnit3Suite {

    def testMinMax {
        val xs = hano.Seq.threaded.generate(Seq(6,3,1,2,6,7,4,2))
        val (ys, zs) = xs.duplicate
        val max = hano.Sync.max(ys)
        val min = hano.Sync.min(zs)
        expect(7)(max())
        expect(1)(min())
    }

    def testLength {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq(3,1,2,6,7,4,2,9))
        val l = hano.Sync.length(xs)
        expect(8)(l())
    }

    def testHead {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq(9,3,1,2,6,7,4,2))
        val head = hano.Sync.head(xs)
        expect(9)(head())
    }

    def testNoHead {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq[Int]())
        val head = hano.Sync.head(xs)
        intercept[NoSuchElementException] {
            head()
        }
    }

    def testLast {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq(3,1,2,6,7,4,2,9))
        val last = hano.Sync.last(xs)
        expect(9)(last())
    }

    def testNoLast {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq[Int]())
        val last = hano.Sync.last(xs)
        intercept[NoSuchElementException] {
            last()
        }
    }

    def testFoldLeft {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq(3,1,2,6,7,4,2,9))
        val fold = hano.Sync.foldLeft(xs)("0")(_ + _.toString)
        expect("031267429")(fold())
    }

    def testFoldLeftEmpty {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq[Int]())
        val fold = hano.Sync.foldLeft(xs)("0")(_ + _.toString)
        expect("0")(fold())

    }

    def testReduceLeft {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq(3,1,2,6,7,4,2,9))
        val reduce = hano.Sync.reduceLeft(xs)(_ + _)
        expect(3+1+2+6+7+4+2+9)(reduce())
    }

    def testReduceLeftEmpty {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(Seq[Int]())
        val reduce = hano.Sync.reduceLeft(xs)(_ + _)
        intercept[NoSuchElementException] {
            reduce()
        }
    }

    def testValThreaded {
        val v = new hano.Sync.Val[Int]
        hano.Seq.origin(hano.eval.Threaded).generate(0 until 10).forloop(v)
        expect(0)(v())
    }

    def testValStrict {
        val v = new hano.Sync.Val[Int]
        hano.Seq.origin(hano.eval.Strict).generate(0 until 10).forloop(v)
        expect(0)(v())
    }

    def testValEmpty {
        val v = new hano.Sync.Val[Int]
        hano.Seq.origin(hano.eval.Threaded).generate(Seq()).forloop(v)
        intercept[NoSuchElementException] {
            v()
        }
    }

/* seems unsolvable
    def testIterable {
        val xs = hano.Seq.origin(hano.eval.Threaded).generate(0 until 108)
        val (ys, zs) = xs.duplicate
        val ax = ys.toIterable
        val bx = zs.toIterable
        expect(hano.util.Iter.from(0 until 108))(hano.util.Iter.from(ax))
        expect(hano.util.Iter.from(0 until 108))(hano.util.Iter.from(bx))
    }
*/
}
