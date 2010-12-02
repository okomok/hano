

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ShiftTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).shift(k => {s.add(99);k;k}).map(_+1)) {
            s.add(x)
        }
        // the last 99 is end-reaction shift.
        assertEquals(hano.util.Vector(99,1,1,99,2,2,99,3,3,99,4,4,99,5,5, 99), hano.util.Vector.from(s))
    }

    def testSwing(off: Int) {
        val s = new java.util.ArrayList[Int]
        val k: (=> Unit) => Any = hano.eval.InEdt[Unit]
        hano.Seq(0,1,2,3,4) shift {
            hano.eval.InEdt
        } map { x =>
            x + 1
        } foreach { x =>
            s.add(x)
        }
        assertEquals(hano.util.Vector(1,2,3,4,5), hano.util.Vector.from(s))
    }

}
