

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class VarTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val rx = new hano.Var(12)
        val out = new java.util.ArrayList[Int]
        rx.foreach(out.add(_))
        rx := 5
        rx := 4
        rx := 6
        assertEquals(hano.util.Vector(12,5,4,6), hano.util.Vector.from(out))
    }

    def testTrivial2 {
        val rx = new hano.Var[Int]
        val out = new java.util.ArrayList[Int]
        rx.foreach(out.add(_))
        rx := 5
        rx := 4
        rx := 6
        assertEquals(hano.util.Vector(5,4,6), hano.util.Vector.from(out))
    }
/* rejected
    def testTrivial3 {
        val rx = new hano.Var[Int]
        val out = new java.util.ArrayList[Int]
        rx := 12
        rx.foreach(out.add(_))
        rx := 5
        rx := 4
        rx := 6
        assertEquals(hano.util.Vector(12, 5,4,6), hano.util.Vector.from(out))
    }

    def testTrivial4 {
        val rx = new hano.Var(10)
        val out = new java.util.ArrayList[Int]
        rx := 12
        rx.foreach(out.add(_))
        rx := 5
        rx := 4
        rx := 6
        assertEquals(hano.util.Vector(12, 5,4,6), hano.util.Vector.from(out))
    }
*/
    def testParallel: Unit = {
    //    for (_ <- 0 to 30) {
            val src = new IntSenders(hano.util.Vector(1,2,3,4,5,6,7,8,9,10), hano.util.Vector(7,7,7,7,7,7,7,7,7,7))
            val dst = new IntReceiver(hano.util.Vector(1,2,3,4,5,6,7,7,7,7,7,7,7,7,7,7,7,8,9,10,10))
            val rx = new hano.Var[Int](10)
            rx.foreach(dst)
            src(0).foreach(rx.:=)
            src(1).foreach(rx.:=)
            src.activate
            src.shutdown(dst.assertMe)
    //    }
    }

    def testSignal {
        val out = new java.util.ArrayList[Int]
        val a = new hano.Var(1)
        val b = new hano.Var(2)
        for (x <- a; y <- b) {
            out.add(x + y)
        }
        assertEquals(hano.util.Vector(3), hano.util.Vector.from(out))
        a := 7
        b := 35
        b := 36
        a := 8
        assertEquals(hano.util.Vector(3,9,42,43,44), hano.util.Vector.from(out))
    }

    def testSignal2 {
        val out = new java.util.ArrayList[Int]
        val a = new hano.Var(1)
        val b = new hano.Var(2)

        a.zip(b).
            collect{ case (x: Int, y: Int) => x + y }.
            foreach{ sum => out.add(sum) }
        a := 7
        b := 35
        assertEquals(hano.util.Vector(3,42), hano.util.Vector.from(out))
    }

    def testSignal3 {
        val out = new java.util.ArrayList[Int]
        val a = new hano.Var[Int](1)
        val b = new hano.Var[Int]//(2)
        val c = new hano.Var[Int](3)
        for (x <- a; y <- b; z <- c) {
            out.add(x + y + z)
        }
        assertEquals(hano.util.Vector.emptyOf[Int], hano.util.Vector.from(out)); out.clear

        b := 2
        assertEquals(hano.util.Vector(1+2+3), hano.util.Vector.from(out)); out.clear

        a := 2
        assertEquals(hano.util.Vector(2+2+3), hano.util.Vector.from(out)); out.clear

        b := 1
        assertEquals(hano.util.Vector(2+1+3), hano.util.Vector.from(out)); out.clear

        c := 4
        assertEquals(hano.util.Vector(2+1+4), hano.util.Vector.from(out)); out.clear

        b := 5
        assertEquals(hano.util.Vector(2+5+4), hano.util.Vector.from(out)); out.clear

        c := 7
        assertEquals(hano.util.Vector(2+5+7), hano.util.Vector.from(out)); out.clear

        a := 8
        assertEquals(hano.util.Vector(8+5+7), hano.util.Vector.from(out)); out.clear

        a := 9
        assertEquals(hano.util.Vector(9+5+7), hano.util.Vector.from(out)); out.clear

        c := 3
        assertEquals(hano.util.Vector(9+5+3), hano.util.Vector.from(out)); out.clear
    }

    def testTwice {
        val out = new java.util.ArrayList[Int]
        val a = new hano.Var(1)
        val b = hano.Var(2)
        for (x <- a; y <- b) {
            out.add(x + y)
        }
        assertEquals(hano.util.Vector(3), hano.util.Vector.from(out))
        a := 7
        b := 35
        b := 36
        a := 8
        assertEquals(hano.util.Vector(3,9,42,43,44), hano.util.Vector.from(out))

        // reset foreach.
        out.clear
        for (x <- a; y <- b) {
            out.add(x - y)
        }
        assertEquals(hano.util.Vector(8 - 36), hano.util.Vector.from(out)); out.clear
        a := 3
        assertEquals(hano.util.Vector(3 - 36), hano.util.Vector.from(out)); out.clear
        b := 5
        assertEquals(hano.util.Vector(3 - 5), hano.util.Vector.from(out)); out.clear
    }

    def testCps {
        val out = new java.util.ArrayList[Int]
        val a = new hano.Var[Int](1)
        val b = hano.Var[Int]//(2)
        val c = new hano.Var[Int](3)
        hano.block { Y =>
            import Y._
            val x = each(a)
            val y = each(b)
            val z = each(c)
            out.add(x + y + z)
        }
        assertEquals(hano.util.Vector.emptyOf[Int], hano.util.Vector.from(out)); out.clear

        b := 2
        assertEquals(hano.util.Vector(1+2+3), hano.util.Vector.from(out)); out.clear

        a := 2
        assertEquals(hano.util.Vector(2+2+3), hano.util.Vector.from(out)); out.clear

        b := 1
        assertEquals(hano.util.Vector(2+1+3), hano.util.Vector.from(out)); out.clear

        c := 4
        assertEquals(hano.util.Vector(2+1+4), hano.util.Vector.from(out)); out.clear

        b := 5
        assertEquals(hano.util.Vector(2+5+4), hano.util.Vector.from(out)); out.clear

        c := 7
        assertEquals(hano.util.Vector(2+5+7), hano.util.Vector.from(out)); out.clear

        a := 8
        assertEquals(hano.util.Vector(8+5+7), hano.util.Vector.from(out)); out.clear

        a := 9
        assertEquals(hano.util.Vector(9+5+7), hano.util.Vector.from(out)); out.clear

        c := 3
        assertEquals(hano.util.Vector(9+5+3), hano.util.Vector.from(out)); out.clear
    }
}
