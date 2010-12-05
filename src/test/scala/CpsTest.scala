

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano
import junit.framework.Assert._
import scala.util.continuations.{shift, suspendable, cpsParam, reset}


class CpsTest extends org.scalatest.junit.JUnit3Suite {

    val t = new java.util.Timer(true)
    def naturals: hano.Seq[Int] = {
        val s: hano.Seq[Unit] = hano.Schedule(t.schedule(_, 0, 100))
        s.generate(Stream.iterate(0)(_ + 1))
    }

    def testTrivial {
        val arr = new java.util.ArrayList[(Int, Int)]
        hano.Block { Y =>
            import Y._
            val x = each(naturals.take(2)) // 0, 1
            val y = each(naturals.take(3)) // 0, 1, 2
            arr.add((x, y))
        }
        Thread.sleep(1200)

        java.util.Collections.sort(arr, implicitly[Ordering[(Int, Int)]])
        assertEquals(hano.util.Iter((0,0), (0,1), (0,2), (1,0), (1,1), (1,2)), hano.util.Iter.from(arr))
    }

    def testNth {
        val arr = new java.util.ArrayList[(Int, Int, Int)]
        hano.Block { Y =>
            import Y._
            val x = nth(naturals)(3)
            val y = head(naturals)
            val z = find(naturals)(_ == 5)
            arr.add((x, y, z))
        }
        Thread.sleep(1200)
        assertEquals(hano.util.Iter((3,0,5)), hano.util.Iter.from(arr))
    }

    def testNthEmpty {
        val arr = new java.util.ArrayList[(Int, Int, Int)]
        hano.Block { Y =>
            import Y._
            val x = nth(naturals.take(3))(4)
            val y = head(naturals)
            val z = find(naturals)(_ == 5)
            arr.add((x, y, z))
        }
        Thread.sleep(1200)
        assertTrue(arr.isEmpty)
    }

    def testFrom {
        val arr = new java.util.ArrayList[Int]
        val xs = hano.Seq.fromCps(shift{(k: Int => Unit) => k(0);k(1);k(2)})
        for (x <- xs) {
            arr.add(x)
        }
        assertEquals(hano.util.Iter(0,1,2), hano.util.Iter.from(arr))
    }

    def testToFrom {
        val arr = new java.util.ArrayList[Int]
        val xs = hano.Seq(0,1,2)
        hano.Block { * =>
            val x = hano.Seq.fromCps(xs.toCps).toCps
            arr.add(x)
        }
        assertEquals(hano.util.Iter(0,1,2), hano.util.Iter.from(arr))
    }


}
