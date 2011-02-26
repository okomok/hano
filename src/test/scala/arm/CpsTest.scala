

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package armtest


import com.github.okomok.hano

import hano.{Arm, block}
import junit.framework.Assert._
import hano.Exit


class CpsTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val r1 = TrivialResource("res1")
        val r2 = TrivialResource("res2")
        val r3 = TrivialResource("res3")
        block {
            val s1 = hano.use(r1).!
            val s2 = hano.use(r2).!
            val s3 = hano.use(r3).!
            assertEquals(s1, "res1")
            assertEquals(s2, "res2")
            assertEquals(s3, "res3")
            999
        }
        assertTrue(r1.began)
        assertTrue(r1.ended)
        assertTrue(r2.began)
        assertTrue(r2.ended)
        assertTrue(r3.began)
        assertTrue(r3.ended)
    }

    def testThrow {
        val r1 = TrivialResource("res1")
        val r2 = TrivialResource("res2")
        val r3 = TrivialResource("res3", true)
        var thrown = false

        try {
            block {
                val s1 = hano.use(r1).!
                val s2 = hano.use(r2).!
                val s3 = hano.use(r3).!
                assertEquals(s1, "res1")
                assertEquals(s2, "res2")
                assertEquals(s3, "res3")
            }
        } catch {
            case _: Error => thrown = true
        }
        assertTrue(thrown)

        assertTrue(r1.began)
        assertTrue(r1.ended)
        assertTrue(r2.began)
        assertTrue(r2.ended)
        assertFalse(r3.began)
        assertFalse(r3.ended)
    }


    val arr = new java.util.ArrayList[Int]

    case class Res1[A](res: A) extends hano.Seq[A] {
        override def context = hano.Self
        override def forloop(f: hano.Reaction[A]) = {
            arr.add(10)
            f.enter()
            try {
                f(res)
            } finally {
                arr.add(11)
            }
        }
    }
    case class Res2[A](res: A) extends hano.Seq[A] {
        override def context = hano.Self
        override def forloop(f: hano.Reaction[A]) = {
            arr.add(20)
            f.enter()
            try {
                f(res)
            } finally {
                arr.add(21)
            }
        }
    }
    case class Res3[A](res: A) extends hano.Seq[A] {
        override def context = hano.Self
        override def forloop(f: hano.Reaction[A]) = {
            arr.add(30)
            f.enter()
            try {
                f(res)
            } finally {
                arr.add(31)
            }
        }
    }

    def testOrder {
        val r1 = Res1("res1")
        val r2 = Res2("res2")
        val r3 = Res3("res3")

        hano.block {
            val s1 = r1.!
            val s2 = r2.!
            val s3 = r3.!
            arr.add(1)
            arr.add(2)
            arr.add(3)
            ()
        }

        assertEquals(hano.Iter(10,20,30,1,2,3,31,21,11), hano.Iter.from(arr))
    }


    def testCloseable {
        val r1 = new TrivialCloseable
        val r2 = new TrivialCloseable
        val r3 = new TrivialCloseable
        block {
            val s1 = hano.use(r1).!
            val s2 = hano.use(r2).!
            val s3 = hano.use(r3).!
            ()
        }
        assertTrue(r1.ended)
        assertTrue(r2.ended)
        assertTrue(r3.ended)
    }
}
