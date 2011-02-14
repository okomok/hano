

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class AlgorithmTest extends org.scalatest.junit.JUnit3Suite {
/*
    def testMinMax {
        val xs = hano.async.loop.generate(Seq(6,3,1,2,6,7,4,2))
        val (ys, zs) = xs.duplicate
        val max = hano.future.max(ys)
        val min = hano.future.min(zs)
        expect(7)(max())
        expect(1)(min())
    }
*/
    def testLength {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val l = new hano.Val[Int]
        l := xs.length
        expect(8)(l())
    }

    def testIsEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val im = new hano.Val[Boolean]
        im := xs.isEmpty
        expect(true)(im())
    }

    def testIsNotEmpty {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val im = new hano.Val[Boolean]
        im := xs.isEmpty
        expect(false)(im())
    }

    def testHead {
        val xs = hano.async.loop.generate(Seq(9,3,1,2,6,7,4,2))
        val head = new hano.Val[Int]
        head := xs.head
        expect(9)(head())
    }

    def testNoHead {
        val xs = hano.async.loop.generate(Seq[Int]())
        val head = new hano.Val[Int]
        head := xs.head
        intercept[NoSuchElementException] {
            head()
        }
    }

    def testLast {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val last = new hano.Val[Int]
        last := xs.last
        expect(9)(last())
    }

    def testNoLast {
        val xs = hano.async.loop.generate(Seq[Int]())
        val last = new hano.Val[Int]
        last := xs.last
        intercept[NoSuchElementException] {
            last()
        }
    }

    def testNth {
        val xs = hano.async.loop.generate(Seq(3,1,8,6,7,4,2,9))
        val nth = hano.Val(xs.nth(6))
        expect(2)(nth())
    }

    def testNoNth {
        val xs = hano.async.loop.generate(Seq(3,1,8,6,7,4,2,9))
        val nth = new hano.Val[Int]
        nth := xs.nth(10)
        intercept[NoSuchElementException] {
            nth()
        }
    }

    def testNoNthEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val nth = new hano.Val[Int]
        nth := xs.nth(0)
        intercept[NoSuchElementException] {
            nth()
        }
    }

    def testFoldLeft {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val fold = new hano.Val[String]
        fold := xs.foldLeft("0")(_ + _.toString)
        expect("031267429")(fold())
    }

    def testFoldLeftEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val fold = new hano.Val[String]
        fold := xs.foldLeft("0")(_ + _.toString)
        expect("0")(fold())

    }

    def testReduceLeft {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val reduce = new hano.Val[Int]
        reduce := xs.reduceLeft(_ + _)
        expect(3+1+2+6+7+4+2+9)(reduce())
    }

    def testReduceLeftEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val reduce = new hano.Val[Int]
        reduce := xs.reduceLeft(_ + _)
        intercept[NoSuchElementException] {
            reduce()
        }
    }

    def testFind {
        val xs = hano.async.loop.generate(Seq(3,1,8,6,7,4,2,9))
        val find = hano.Val(xs.find(_ == 6))
        expect(6)(find())
    }

    def testNoFind {
        val xs = hano.async.loop.generate(Seq(3,1,8,6,7,4,2,9))
        val find = new hano.Val[Int]
        find := xs.find(_ == 10)
        intercept[NoSuchElementException] {
            find()
        }
    }

    def testNoFindEmpty {
        val xs = hano.async.loop.generate(Seq[Int]())
        val find = new hano.Val[Int]
        find := xs.find(_ == 0)
        intercept[NoSuchElementException] {
            find()
        }
    }

/* hmmmmmm....
    def testCopy {
        val xs = hano.async.loop.generate(Seq(3,1,2,6,7,4,2,9))
        val v = new hano.Val[scala.collection.immutable.Vector[Int]]
        v := xs.copy//(implicitly[scala.collection.generic.CanBuildFrom[Nothing, Int, scala.collection.immutable.Vector[Int]]])
        expect(hano.Iter(3,1,2,6,7,4,2,9))(hano.Iter.from(v()))
    }
*/
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