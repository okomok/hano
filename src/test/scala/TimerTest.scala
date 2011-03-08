

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class TimerTezt {
//extends org.scalatest.junit.JUnit3Suite {

    val t = new hano.Timer(true)
    def naturals: hano.Seq[Int] = {
        val s: hano.Seq[Unit] = t.schedule(0, 100)
        s.pull(Stream.iterate(0)(_ + 1))
    }

    // close method access in hano.byName generates sequences forever, which results in stack overflow.
    def testRecursive: Unit = {
        val b = new java.util.ArrayList[Int]
        def rx: hano.Seq[Int] = naturals.take(3) ++ hano.byName(rx)
        rx.take(10).foreach(b.add(_))
        Thread.sleep(2000)
        assertEquals(hano.Iter(0,1,2,0,1,2,0,1,2,0), hano.Iter.from(b))
    }

}


class TimerProcessTest extends org.scalatest.junit.JUnit3Suite {

    val t = new hano.Timer(true)
    def testConforming {
        for (i <- 0 until 10) {
            val out = new java.util.ArrayList[Int]
            t eval { out.add(0) }
            t eval { out.add(1) }
            t eval { out.add(2) }
            t eval { out.add(3) }
            t eval { out.add(4) }
            Thread.sleep(100)
            t eval { out.add(5) }
            Thread.sleep(100)
            expect(hano.Iter.from(0 until 6))(hano.Iter.from(out))
        }
    }
}
