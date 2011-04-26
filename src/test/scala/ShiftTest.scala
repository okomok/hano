

// Copyright Shunsuke Sogame 2010-2011.
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
//        val k: (=> Unit) => Any = hano.invoke.Edt[Unit]
        hano.Seq(0,1,2,3,4) shift {
            hano.Edt
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
        hano.async.pull(0 to 10) shift {
            hano.Self
        } foreach { x =>
            s.add(x)
            Thread.sleep(100)
            expect(cur)(Thread.currentThread())
        }
        expect(hano.Iter.from(0 to 10))(hano.Iter.from(s))
    }

    def testAsynchronous {
        val xs = hano.Self.pull(0 until 500).asynchronous
        expect(hano.Iter.from(0 until 500))(xs.toIter)
    }

    def testShiftStartFromSelf {
        val xs = hano.Self.pull(0 until 500).shiftStart(hano.async)
        expect(hano.Iter.from(0 until 500))(xs.toIter)
    }

    def testShiftStartFromOther {
        val xs = hano.async.pull(0 until 500).shiftStart(hano.async)
        expect(hano.Iter.from(0 until 500))(xs.toIter)
    }

    def testShiftFusion1 {
        val xs = hano.async.pull(0 until 5).shift(hano.Edt).shift(hano.Edt)
        xs.onEach { _ =>
            assert(javax.swing.SwingUtilities.isEventDispatchThread)
        } await()
    }

    def testShiftFusion2 {
        val xs = hano.async.pull(0 until 5).shift(hano.Edt).shift(hano.async)
        xs.onEach { _ =>
            expect(false)(javax.swing.SwingUtilities.isEventDispatchThread)
        } await()
    }
}


class ShiftAAsyncShiftTest extends org.scalatest.junit.JUnit3Suite {

    def testAsyncToAsync {

        for (i <- 0 until 10) {
            val gate = new java.util.concurrent.CountDownLatch(1)
            hano.async.pull {
                Iterator.from(0)
            }.onExit { q =>
                gate.countDown()
            }.shift {
                hano.async
            }.drop(10).onHead {
                case Some(n) => {
                    //println("breaking")
                    hano.break
                }
                case None => {
                    //println("not found")
                }
            }.start()

            assert(gate.await(5000, java.util.concurrent.TimeUnit.MILLISECONDS))
        }
    }

}
