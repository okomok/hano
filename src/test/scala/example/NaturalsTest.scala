

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest;
package sequencetest; package reactivetest; package example


import com.github.okomok.hano


class NaturalsTezt {
//class NaturalsTest extends org.scalatest.junit.JUnit3Suite {

    def naturals: hano.Seq[Int] = {
        val t = new java.util.Timer(true)
        val s: hano.Seq[Unit] = hano.Schedule(t.schedule(_, 0, 1000))
        s.generate(Stream.iterate(0)(_ + 1))
    }

    def testTrivial {
        naturals.take(10).foreach(println(_))
        Thread.sleep(12000)
    }

}
