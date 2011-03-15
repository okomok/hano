

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class LatestTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val xs = hano.Timer.daemon.schedule(0, 300).pull(0 until 50)
        val it = xs.latest().iterator
        Thread.sleep(700)
        expect(0)(it.next)
        Thread.sleep(700)
        expect(2)(it.next)
        Thread.sleep(700)
        expect(4)(it.next)

        it match {
            case it: java.io.Closeable => it.close()
            case _ => fail("iterator shall be closeable")
        }
    }
}
