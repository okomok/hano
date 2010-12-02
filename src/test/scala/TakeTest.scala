

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class TakeTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial0: Unit = {
        val a = hano.util.Iterable(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).take(3).foreach(b.add(_))
        assertEquals(hano.util.Iterable(1,2,3), hano.util.Iterable.from(b))
    }

    def testTrivial {
        val t = hano.Seq(4,5,1,3,2,9,7,10)
        val k = t.take(5)
        assertEquals(hano.util.Iterable(4,5,1,3,2), k.toIterable)
        val k_ = t.take(50)
        assertEquals(hano.util.Iterable(4,5,1,3,2,9,7,10), k_.toIterable)
        assertTrue(t.take(0).toIterable.isEmpty)
    }

    def testThen: Unit = {
        val a = hano.util.Iterable(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).take(3).onExit(_ =>b.add(99)).foreach(b.add(_))
        assertEquals(hano.util.Iterable(1,2,3,99), hano.util.Iterable.from(b))
    }

    def testThen2: Unit = {
        val a = hano.util.Iterable(1,2,3,4,5,6)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).take(3).onExit(_ =>b.add(98)).onExit(_ =>b.add(99)).foreach(b.add(_))
        assertEquals(hano.util.Iterable(1,2,3,98,99), hano.util.Iterable.from(b))
    }
/*
    def testThenNotEnough: Unit = {
        val a = hano.util.Iterable(1,2,3,4,5)
        val b = new java.util.ArrayList[Int]
        hano.Seq.from(a).take(30).onExit(_ =>b.add(99)).foreach(b.add(_))
        assertEquals(hano.util.Iterable(1,2,3,4,5), hano.util.Iterable.from(b))
    }
*/
    def testThenAppend: Unit = {
        val a = hano.util.Iterable(1,2,3,4,5)
        val b = new java.util.ArrayList[Int]
        (hano.Seq.from(a).take(3) ++ hano.Seq(6,7,8)).foreach(b.add(_))
        assertEquals(hano.util.Iterable(1,2,3,6,7,8), hano.util.Iterable.from(b))
    }

    def testThenAppendThen: Unit = {
        val a = hano.util.Iterable(1,2,3,4,5)
        val b = new java.util.ArrayList[Int]
        (hano.Seq.from(a).take(3) ++ hano.Seq(6,7,8)).onExit(_ =>b.add(99)).foreach(b.add(_))
        assertEquals(hano.util.Iterable(1,2,3,6,7,8,99), hano.util.Iterable.from(b))
    }

}
