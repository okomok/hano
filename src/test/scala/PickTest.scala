

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class PickTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val xs = hano.Timer.nondaemon.schedule(500, 500).pull(Seq(0,1,2,3,4,5))
        val it = xs.pick.iterator
        Thread.sleep(600)
        expect(Some(0))(it.next)
        Thread.sleep(600)
        expect(Some(1))(it.next)
        Thread.sleep(600)
        expect(Some(2))(it.next)
        it match {
            case it: java.io.Closeable => it.close()
            case _ => fail("iterator shall be closeable")
        }
    }
}
