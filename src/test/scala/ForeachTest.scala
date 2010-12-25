

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ForeachTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val a = hano.util.Iter(1,6,7,10,14,17)
        val t = new java.util.ArrayList[Int]
        hano.Seq.from(a).foreach{ e => t.add(e) }
        assertEquals(a, hano.util.Iter.from(t))
    }
/*
    def testOnEnd: Unit = {
        val a = hano.util.Iter(1,6,7,10,14,17)
        val t = new java.util.ArrayList[Int]

        val c = new Call(assertEquals(a, hano.util.Iter.from(t)))

        hano.Seq.from(a).activate(new Reactor[Int] {
            override def onEnd = c()
            override def onEach(e: Int) = t.add(e)
        })

        assertTrue(c.isCalled)
    }

    def testRun: Unit = {
        val a = hano.util.Iter(1,6,7,10,14,17)
        val t = new java.util.ArrayList[Int]
        hano.Seq.from(a).forkTo{ reactor.make(_ => (), e => t.add(e)) }.start
        assertEquals(a, hano.util.Iter.from(t))
    }
*/
}
