

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.madatest; package sequencetest
package reactivetest; package example


import com.github.okomok.hano


    import junit.framework.Assert._

    class FibonacciTezt {
//    class FibonacciTest extends org.scalatest.junit.JUnit3Suite {
        val t = new java.util.Timer(true)
        def naturals: hano.Seq[Int] = {
            val s: hano.Seq[Unit] = hano.Schedule(t.schedule(_, 0, 1000))
            s.generate(hano.util.Iterable.iterate(0)(_ + 1))
        }
        def testTrivial: Unit = {
            // too many instances.
            def fibs: hano.Seq[Int] = naturals.take(2) ++ ((fibs zip fibs.tail).map2(_ + _)).byName
            var answer: Int = 0
            fibs.foreach(println(_))
        }
    }