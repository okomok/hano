

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class ZipWithTimeTest extends org.scalatest.junit.JUnit3Suite {

    val t = new hano.Timer(true)
    def naturals: hano.Seq[Int] = {
        val s: hano.Seq[Unit] = t.schedule(0, 1000)
        s.pull(Stream.iterate(0)(_ + 1))
    }

    def testTrivial {
        naturals.zipWith(Iterator.continually(new java.util.Date)).take(10).onEach { x =>
            println(x)
        } start()
    }

    def test_ {}

}
