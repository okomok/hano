

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class TimerTezt {
//extends org.scalatest.junit.JUnit3Suite {

    val t = new java.util.Timer(true)
    def naturals: hano.Seq[Int] = {
        val s: hano.Seq[Unit] = hano.Schedule(t.schedule(_, 0, 100))
        s.generate(hano.util.Iterable.iterate(0)(_ + 1))
    }

    // close method access in hano.ByName generates sequences forever, which results in stack overflow.
    def testRecursive: Unit = {
        val b = new java.util.ArrayList[Int]
        def rx: hano.Seq[Int] = naturals.take(3) ++ rx.byName
        rx.take(10).foreach(b.add(_))
        Thread.sleep(2000)
        assertEquals(hano.util.Iterable(0,1,2,0,1,2,0,1,2,0), hano.util.Iterable.from(b))
    }

}
