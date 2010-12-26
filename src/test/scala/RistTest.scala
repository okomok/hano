

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class RistTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val rx = hano.Rist(12,13)
        val out = new java.util.ArrayList[Int]
        rx.foreach(out.add(_))
        rx add 5
        rx add 4
        rx add 6
        assertEquals(hano.util.Iter(12,13,5,4,6), hano.util.Iter.from(out))
    }

    def testTrivial2 {
        val rx = hano.Rist[Int]()
        val out = new java.util.ArrayList[Int]
        rx.foreach(out.add(_))
        rx add 5
        rx add 4
        rx add 6
        assertEquals(hano.util.Iter(5,4,6), hano.util.Iter.from(out))
    }
/*
    def testParallel: Unit = {
    //    for (_ <- 0 to 30) {
            val src = new IntSenders(hano.util.Iter(1,2,3,4,5,6,7,8,9,10), hano.util.Iter(7,7,7,7,7,7,7,7,7,7))
            val dst = new IntReceiver(hano.util.Iter(1,2,3,4,5,6,7,7,7,7,7,7,7,7,7,7,7,8,9,10,10))
            val rx = hano.Rist[Int](10)
            rx.foreach(dst)
            src(0).foreach(rx.add)
            src(1).foreach(rx.add)
            src.activate
            src.shutdown(dst.assertMe)
    //    }
    }
*/
    def testSignal {
        val out = new java.util.ArrayList[Int]
        val a = hano.Rist(1)
        val b = hano.Rist(2)
        for (x <- a; y <- b) {
            out.add(x + y)
        }
        a add 7
        b add 35
        assertEquals(hano.util.Iter(3,9,36,42), hano.util.Iter.from(out))

    }

    def testSignal2 {
        val out = new java.util.ArrayList[Int]
        val a = hano.Rist(1)
        val b = hano.Rist(2)

        a.zip(b).
            collect{ case (x: Int, y: Int) => x + y }.
            foreach{ sum => out.add(sum) }
        a add 7
        b add 35
        assertEquals(hano.util.Iter(3,42), hano.util.Iter.from(out))
    }

    def testSignal3 {
        val out = new java.util.ArrayList[Int]
        val a = hano.Rist(1,2,3)
        val b = hano.Rist(4,5)
        for (x <- a; y <- b) {
            out.add(x + y)
        }
        assertEquals(hano.util.Iter(5,6,6,7,7,8), hano.util.Iter.from(out))
        a add 7
        assertEquals(hano.util.Iter(5,6,6,7,7,8,11,12), hano.util.Iter.from(out))
        b add 35
        assertEquals(hano.util.Iter(5,6,6,7,7,8,11,12,36,37,38,42), hano.util.Iter.from(out))
    }


    def testTwice {
        val out = new java.util.ArrayList[Int]
        val a = hano.Rist(1,2,3)
        val b = hano.Rist(4,5)
        for (x <- a; y <- b) {
            out.add(x + y) //  old listner
        }
        assertEquals(hano.util.Iter(5,6,6,7,7,8), hano.util.Iter.from(out))
        a add 7
        assertEquals(hano.util.Iter(5,6,6,7,7,8,11,12), hano.util.Iter.from(out))
        b add 35
        assertEquals(hano.util.Iter(5,6,6,7,7,8,11,12,36,37,38,42), hano.util.Iter.from(out))

        // now, a: 1,2,3,7, b:4,5,35
        out.clear
        for (x <- a; y <- b) {
            out.add(x * y)
        }
        assertEquals(hano.util.Iter(4,5,35, 8,10,70, 12,15,105, 28,35,35*7), hano.util.Iter.from(out))
        out.clear
        a add 10 // now, a:1,2,3,7,10
        assertEquals(hano.util.Iter(14,15,35+10,  40,50,350), hano.util.Iter.from(out)) // old listner still listen.
        out.clear
        b add 30
        assertEquals(hano.util.Iter(31,32,33,37,  30,30*2,30*3,30*7,  30+10, 30*10), hano.util.Iter.from(out))

    }

    def testToReaction {
        val out = new java.util.ArrayList[Int]
        val a = hano.Rist(1,2,3)
        for (x <- a) {
            out.add(x)
        }
        hano.Seq(9,10,11).forloop(a)
        expect(hano.util.Iter(1,2,3,9,10,11))(hano.util.Iter.from(out))
    }
}
