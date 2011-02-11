

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ShiftTest extends org.scalatest.junit.JUnit3Suite {

/*
    def testTrivial: Unit = {
        val s = new java.util.ArrayList[Int]
        for (x <- hano.Seq(0,1,2,3,4).shiftBy(k => {s.add(99);k/*;k*/}).map(_+1)) {
            s.add(x)
        }
        // the last 99 is end-reaction shift.
        assertEquals(hano.Iter(99,1,99,2,99,3,99,4,99,5, 99), hano.Iter.from(s))
    }
*/
    def testSwing(off: Int) {
        val s = new java.util.ArrayList[Int]
//        val k: (=> Unit) => Any = hano.eval.InEdt[Unit]
        hano.Seq(0,1,2,3,4) shift {
            hano.InEdt
        } map { x =>
            x + 1
        } foreach { x =>
            s.add(x)
        }
        assertEquals(hano.Iter(1,2,3,4,5), hano.Iter.from(s))
    }

    def testAsyncToSelf {
        val s = new java.util.ArrayList[Int]
        val cur = Thread.currentThread()
        hano.async.loop.generate(0 to 10) shift {
            hano.Self
        } foreach { x =>
            s.add(x)
            Thread.sleep(100)
            expect(cur)(Thread.currentThread())
        }
        expect(hano.Iter.from(0 to 10))(hano.Iter.from(s))
    }
}
