

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._
import scala.collection.immutable.{Vector, IndexedSeq}
import scala.collection.JavaConversions._


class BufferedWithinTest extends org.scalatest.junit.JUnit3Suite {
/*
    implicit def _buffered[A](from: hano.Seq[A]) = new {
        def bufferedWithin[To](n: Long, b: => scala.collection.mutable.Builder[A, To] = hano.Seq.defaultBuilder[A]) = new hano.detail.BufferedWithin(from, n, () => b)
    }
*/
    def teztTrivial: Unit = {
        val xs = hano.Timer.daemon.schedule(0, 100).pull(0 until 100)
        val out = new java.util.ArrayList[IndexedSeq[Int]]
        xs.bufferedWithin(0).foreach { x =>
            println(x)
        }

//        assertEquals(Vector(Vector(1,2),Vector(3,4)), Vector.empty ++ out)
    }

    def test_() {}

}
