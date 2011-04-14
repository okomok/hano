

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class ThrottleTest extends org.scalatest.junit.JUnit3Suite {

    val t = new hano.Timer(false)

    def aSample: hano.Seq[Int] = {
        val s: hano.Seq[Unit] = t.schedule(0, 100)
        s.pull(Stream.iterate(0)(_ + 1)).subseq(Seq(0,4,6,10,12,13,19,20))
    }

    def testTrivial {
        val xs = aSample.throttle(350)
        expect(List(0,6,13))(xs.take(3).toList)
    }

    def testLastIsOK {
        val xs = aSample.throttle(350)
        expect(List(0,6,13,20))(xs.take(4).toList)
    }

    def testProcessBack {
        var ct: Thread = null
        aSample.take(1).onEach { _ =>
            ct = Thread.currentThread
        } await()

        aSample.throttle(350).take(1).onEach { _ =>
            expect(ct)(Thread.currentThread)
        } await()
    }
}
