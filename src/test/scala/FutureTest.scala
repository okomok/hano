

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class FutureTest extends org.scalatest.junit.JUnit3Suite {

    def testMinMax {
        val xs = hano.async.loop.generate(Seq(6,3,1,2,6,7,4,2))
        val (ys, zs) = xs.duplicate
        val max = hano.future.max(ys)
        val min = hano.future.min(zs)
        expect(7)(max())
        expect(1)(min())
    }

    def testLength {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val l = hano.future.length(xs)
        expect(8)(l())
    }

    def testIsEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val im = hano.future.isEmpty(xs)
        expect(true)(im())
    }

    def testIsNotEmpty {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val im = hano.future.isEmpty(xs)
        expect(false)(im())
    }

    def testHead {
        val xs = hano.async.loop.generate(Seq(9,3,1,2,6,7,4,2))
        val head = hano.future.head(xs)
        expect(9)(head())
    }

    def testNoHead {
        val xs = hano.async.loop.generate(Seq[Int]())
        val head = hano.future.head(xs)
        intercept[NoSuchElementException] {
            head()
        }
    }

    def testLast {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val last = hano.future.last(xs)
        expect(9)(last())
    }

    def testNoLast {
        val xs = hano.async.loop.generate(Seq[Int]())
        val last = hano.future.last(xs)
        intercept[NoSuchElementException] {
            last()
        }
    }

    def testNth {
        val xs = hano.async.loop.generate(Seq(3,1,8,6,7,4,2,9))
        val nth = hano.future.nth(xs)(6)
        expect(2)(nth())
    }

    def testNoNth {
        val xs = hano.async.loop.generate(Seq(3,1,8,6,7,4,2,9))
        val nth = hano.future.nth(xs)(10)
        intercept[NoSuchElementException] {
            nth()
        }
    }

    def testNoNthEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val nth = hano.future.nth(xs)(0)
        intercept[NoSuchElementException] {
            nth()
        }
    }

    def testFoldLeft {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val fold = hano.future.foldLeft(xs)("0")(_ + _.toString)
        expect("031267429")(fold())
    }

    def testFoldLeftEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val fold = hano.future.foldLeft(xs)("0")(_ + _.toString)
        expect("0")(fold())

    }

    def testReduceLeft {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val reduce = hano.future.reduceLeft(xs)(_ + _)
        expect(3+1+2+6+7+4+2+9)(reduce())
    }

    def testReduceLeftEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val reduce = hano.future.reduceLeft(xs)(_ + _)
        intercept[NoSuchElementException] {
            reduce()
        }
    }

    def testValThreaded {
        val v = new hano.future.Val[Int]
        hano.async.loop.generate(0 until 10).forloop(v)
        expect(0)(v.get)
    }

    def testValStrict {
        val v = new hano.future.Val[Int]
        hano.async.loop.generate(0 until 10).forloop(v)
        expect(0)(v.get)
    }

    def testValEmpty {
        val v = new hano.future.Val[Int]
        hano.async.loop.generate(Seq()).forloop(v)
        intercept[NoSuchElementException] {
            v.get
        }
    }

    def testCopy {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val v: () => scala.collection.immutable.Vector[Int] = hano.future.copy(xs)
        expect(hano.Iter(3,1,2,6,7,4,2,9))(hano.Iter.from(v()))
    }

    def testBreakOut {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val v: scala.collection.immutable.Vector[Int] = xs.breakOut
        expect(hano.Iter(3,1,2,6,7,4,2,9))(hano.Iter.from(v))
    }

/* seems unsolvable
    def testIterable {
        val xs = hano.async.loop.generate(0 until 108)
        val (ys, zs) = xs.duplicate
        val ax = ys.toIterable
        val bx = zs.toIterable
        expect(hano.Iter.from(0 until 108))(hano.Iter.from(ax))
        expect(hano.Iter.from(0 until 108))(hano.Iter.from(bx))
    }
*/
}
