

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class NewsTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val xs = hano.Timer.nondaemon.schedule(500, 500).pull(0 until 6)
        val it = xs.news(999).iterator
        expect(999)(it.next)
        Thread.sleep(600)
        expect(0)(it.next)
        Thread.sleep(600)
        expect(1)(it.next)
        Thread.sleep(600)
        expect(2)(it.next)
        it match {
            case it: java.io.Closeable => it.close()
            case _ => fail("iterator shall be closeable")
        }
    }
}
