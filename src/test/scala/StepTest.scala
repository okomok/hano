

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class StepTest extends org.scalatest.junit.JUnit3Suite {
/*
    def testStep0: Unit = {
        val out = new java.util.ArrayList[Int]
        hano.Seq(1,2,3,4,5,6).step(0).take(5).activate(reactor.make(_ => out.add(99), out.add(_)))
        assertEquals(hano.Iter(1,1,1,1,1, 99), hano.Iter.from(out))
    }
*/
    def testStep1: Unit = {
        val out = new java.util.ArrayList[Int]
        hano.Seq(1,2,3,4,5,6).step(1).foreach(out.add(_))
        assertEquals(hano.Iter(1,2,3,4,5,6), hano.Iter.from(out))
    }

    def testStep2: Unit = {
        val out = new java.util.ArrayList[Int]
        hano.Seq(1,2,3,4,5,6).step(2).foreach(out.add(_))
        assertEquals(hano.Iter(1,3,5), hano.Iter.from(out))
    }

    def testStep3: Unit = {
        val out = new java.util.ArrayList[Int]
        hano.Seq(1,2,3,4,5,6).step(3).foreach(out.add(_))
        assertEquals(hano.Iter(1,4), hano.Iter.from(out))
    }

    def testStepFusion: Unit = {
        val out = new java.util.ArrayList[Int]
        hano.Seq(1,2,3,4,5,6,7,8,9,10,11).step(3).step(2).foreach(out.add(_))
        assertEquals(hano.Iter(1,7), hano.Iter.from(out))
    }
/*
    def testStep02Fusion: Unit = {
        val out = new java.util.ArrayList[Int]
        hano.Seq(1,2,3,4,5,6).step(0).step(2).take(3).activate(reactor.make(_ => out.add(99), out.add(_)))
        assertEquals(hano.Iter(1,1,1, 99), hano.Iter.from(out))
    }

    def testStep20Fusion: Unit = {
        val out = new java.util.ArrayList[Int]
        hano.Seq(1,2,3,4,5,6).step(0).step(2).take(3).activate(reactor.make(_ => out.add(99), out.add(_)))
        assertEquals(hano.Iter(1,1,1, 99), hano.Iter.from(out))
    }
*/
    def testStepEmpty: Unit = {
        val out = new java.util.ArrayList[Int]
/*
        hano.Empty.of[Int].step(0).activate(reactor.make(_ => out.add(99), out.add(_)))
        assertEquals(hano.Iter(99), hano.Iter.from(out))
        out.clear
*/
        hano.Empty.of[Int].step(1).foreach(out.add(_))
        assertTrue(out.isEmpty)
        out.clear

        hano.Empty.of[Int].step(2).foreach(out.add(_))
        assertTrue(out.isEmpty)
        out.clear

        ()
    }

}
