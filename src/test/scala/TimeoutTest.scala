

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class TimeoutTest extends org.scalatest.junit.JUnit3Suite {

    val t = new hano.Timer(true)
    def naturals: hano.Seq[Int] = {
        val s: hano.Seq[Unit] = t.schedule(500, 100)
        s.pull(Stream.iterate(0)(_ + 1))
    }

    def testSuccess {
        val xs = naturals.timeout(hano.Timer.after(700)).take(3)
        expect(hano.Iter(0,1,2))(xs.toIter)
    }

    def testFailure {
        intercept[java.util.concurrent.TimeoutException] {
            naturals.timeout(hano.Timer.after(200)).take(3).await()
        }
    }
}
