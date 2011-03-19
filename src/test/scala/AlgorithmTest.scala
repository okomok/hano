

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class AlgorithmTest extends org.scalatest.junit.JUnit3Suite {
/*
    def testMinMax {
        val xs = hano.async.pull(Seq(6,3,1,2,6,7,4,2))
        val (ys, zs) = xs.duplicate
        val max = hano.future.max(ys)
        val min = hano.future.min(zs)
        expect(7)(max())
        expect(1)(min())
    }
*/
    def testLength {
        val xs = hano.async.pull(Seq(3,1,2,6,7,4,2,9))
        val l = new hano.Val[Int]
        l := xs.length
        expect(8)(l())
    }

    def testIsEmpty {
        val xs = hano.async.pull(Seq[Int]())
        val im = new hano.Val[Boolean]
        im := xs.isEmpty
        expect(true)(im())
    }

    def testIsNotEmpty {
        val xs = hano.async.pull(Seq(3,1,2,6,7,4,2,9))
        val im = new hano.Val[Boolean]
        im := xs.isEmpty
        expect(false)(im())
    }

    def testHead {
        val xs = hano.async.pull(Seq(9,3,1,2,6,7,4,2))
        val head = new hano.Val[Int]
        head := xs.head
        expect(9)(head())
    }

    def testNoHead {
        val xs = hano.async.pull(Seq[Int]())
        val head = new hano.Val[Int]
        head := xs.head
        intercept[NoSuchElementException] {
            head()
        }
    }

    def testLast {
        val xs = hano.async.pull(Seq(3,1,2,6,7,4,2,9))
        val last = new hano.Val[Int]
        last := xs.last
        expect(9)(last())
    }

    def testNoLast {
        val xs = hano.async.pull(Seq[Int]())
        val last = new hano.Val[Int]
        last := xs.last
        intercept[NoSuchElementException] {
            last()
        }
    }

    def testNth {
        val xs = hano.async.pull(Seq(3,1,8,6,7,4,2,9))
        val nth = hano.Val(xs.nth(6))
        expect(2)(nth())
    }

    def testNoNth {
        val xs = hano.async.pull(Seq(3,1,8,6,7,4,2,9))
        val nth = new hano.Val[Int]
        nth := xs.nth(10)
        intercept[NoSuchElementException] {
            nth()
        }
    }

    def testNoNthEmpty {
        val xs = hano.async.pull(Seq[Int]())
        val nth = new hano.Val[Int]
        nth := xs.nth(0)
        intercept[NoSuchElementException] {
            nth()
        }
    }

    def testFoldLeft {
        val xs = hano.async.pull(Seq(3,1,2,6,7,4,2,9))
        val fold = new hano.Val[String]
        fold := xs.foldLeft("0")(_ + _.toString)
        expect("031267429")(fold())
    }

    def testFoldLeftEmpty {
        val xs = hano.async.pull(Seq[Int]())
        val fold = new hano.Val[String]
        fold := xs.foldLeft("0")(_ + _.toString)
        expect("0")(fold())

    }

    def testReduceLeft {
        val xs = hano.async.pull(Seq(3,1,2,6,7,4,2,9))
        val reduce = new hano.Val[Int]
        reduce := xs.reduceLeft(_ + _)
        expect(3+1+2+6+7+4+2+9)(reduce())
    }

    def testReduceLeftEmpty {
        val xs = hano.async.pull(Seq[Int]())
        val reduce = new hano.Val[Int]
        reduce := xs.reduceLeft(_ + _)
        intercept[NoSuchElementException] {
            reduce()
        }
    }

    def testFind {
        val xs = hano.async.pull(Seq(3,1,8,6,7,4,2,9))
        val find = hano.Val(xs.find(_ == 6))
        expect(6)(find())
    }

    def testNoFind {
        val xs = hano.async.pull(Seq(3,1,8,6,7,4,2,9))
        val find = new hano.Val[Int]
        find := xs.find(_ == 10)
        intercept[NoSuchElementException] {
            find()
        }
    }

    def testNoFindEmpty {
        val xs = hano.async.pull(Seq[Int]())
        val find = new hano.Val[Int]
        find := xs.find(_ == 0)
        intercept[NoSuchElementException] {
            find()
        }
    }

    def testForallYes {
        val xs = hano.async.pull(Seq(2,4,6,8,10))
        val v = new hano.Val[Boolean]
        v := xs.forall(_ % 2 == 0)
        expect(true)(v())
    }

    def testForallNo {
        val xs = hano.async.pull(Seq(2,4,6,7,8,10))
        val v = new hano.Val[Boolean]
        v := xs.forall(_ % 2 == 0)
        expect(false)(v())
    }

    def testExistsYes {
        val xs = hano.async.pull(Seq(2,4,6,8,10))
        val v = new hano.Val[Boolean]
        v := xs.exists(_ == 8)
        expect(true)(v())
    }

    def testExistsNo {
        val xs = hano.async.pull(Seq(2,4,6,7,8,10))
        val v = new hano.Val[Boolean]
        v := xs.exists(_ == 9)
        expect(false)(v())
    }

    def testMin {
        val xs = hano.async.pull(Seq(8,5,1,2,10,4,3,9))
        val v = new hano.Val[Int]
        v := xs.min
        expect(1)(v())
    }

    def testMax {
        val xs = hano.async.pull(Seq(8,5,1,2,10,4,3,9))
        val v = new hano.Val[Int]
        v := xs.max
        expect(10)(v())
    }

    def testOrElse {
        val xs = hano.async.pull(Seq[Int]())
        val v = new hano.Val[Int]
        v := xs.max.orElse(10)
        expect(10)(v())
    }

    def testSum {
        val xs = hano.async.pull(Seq(1,2,3,4,5))
        val v = new hano.Val[Int]
        v := xs.sum
        expect(1+2+3+4+5)(v())
    }

    def testProduct {
        val xs = hano.async.pull(Seq(1,2,3,4,5))
        val v = new hano.Val[Int]
        v := xs.product
        expect(1*2*3*4*5)(v())
    }

    def testFoldSuccess {
        val xs = hano.async.pull(Seq(1,2,3,4,5))
        val v = new hano.Val[Int]
        v := xs.max.product
        expect(5)(v())
    }

    def testCount {
        val xs = hano.async.pull(Seq(3,1,8,6,7,4,2,9))
        val find = hano.Val(xs.count(_ % 2 == 0))
        expect(4)(find())
    }

    def testCopy {
        val xs = hano.async.pull(Seq(3,1,2,6,7,4,2,9))
        val v = new hano.Val[scala.collection.immutable.Vector[Int]]
        v := xs.copy()
        expect(hano.Iter(3,1,2,6,7,4,2,9))(hano.Iter.from(v()))
    }

    def testCopyNoDefault {
        import scala.collection.mutable.ArrayBuffer
        val xs = hano.async.pull(Seq(3,1,2,6,7,4,2,9))
        val v = new hano.Val[ArrayBuffer[Int]]
        v := xs.copy(ArrayBuffer.newBuilder)
        expect(hano.Iter(3,1,2,6,7,4,2,9))(hano.Iter.from(v()))
    }

    def testBreakOut {
        val xs = hano.async.pull(Seq(3,1,2,6,7,4,2,9))
        val v: scala.collection.immutable.Vector[Int] = xs.breakOut
        expect(hano.Iter(3,1,2,6,7,4,2,9))(hano.Iter.from(v))
    }

/* seems unsolvable
    def testIterable {
        val xs = hano.async.pull(0 until 108)
        val (ys, zs) = xs.duplicate
        val ax = ys.toIterable
        val bx = zs.toIterable
        expect(hano.Iter.from(0 until 108))(hano.Iter.from(ax))
        expect(hano.Iter.from(0 until 108))(hano.Iter.from(bx))
    }
*/
}
