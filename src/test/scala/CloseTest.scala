

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class CloseTest extends org.scalatest.junit.JUnit3Suite {

    class MyResource extends hano.NoExitResource[Int] {
        private var out: Int => Unit = null
        var closed = false
        override def context = hano.Context.self
        override protected def openResource(f: Int => Unit) = {
            out = f
        }
        override protected def closeResource = closed = true
        def gen(i: Int) = out(i)
    }

    def testTrivial {
        val r = new MyResource
        r.take(3).start
        r.gen(3)
        r.gen(3)
        assertFalse(r.closed)
        r.gen(3)
        assertTrue(r.closed)
    }

    def testProtect {
        val r = new MyResource
        r.protect.take(3).start
        r.gen(3)
        r.gen(3)
        assertFalse(r.closed)
        r.gen(3)
        assertFalse(r.closed)
    }

    def testTake0 {
        val r = new MyResource
        assertFalse(r.closed)
        r.take(0).start
        assertTrue(r.closed)
    }

    def testChain {
        val r = new MyResource
        r.onEach(_ => ()).filter(_ => true).take(3).start
        r.gen(3)
        r.gen(3)
        assertFalse(r.closed)
        r.gen(3)
        assertTrue(r.closed)
    }

    def testMerge {
        val l = new MyResource
        val r = new MyResource
        l.take(5).merge(r.take(8)).take(3).start
        l.gen(3)
        r.gen(3)
        assertFalse(l.closed)
        assertFalse(r.closed)
        r.gen(3)
        assertTrue(l.closed)
        assertTrue(r.closed)
    }

    def testFork {
        val r = new MyResource
        r.fork{s => s.take(5)}.take(3).start
        r.gen(3)
        r.gen(3)
        r.gen(3)
        assertFalse(r.closed)
        r.gen(3)
        r.gen(3)
        assertTrue(r.closed)
    }

    def testZip {
        val l = new MyResource
        val r = new MyResource
        l.take(5).zip(r.take(8)).take(3).start
        l.gen(3); r.gen(3)
        l.gen(3); r.gen(3)
        assertFalse(l.closed)
        assertFalse(r.closed)
        l.gen(3); r.gen(3)
        assertTrue(l.closed)
        assertTrue(r.closed)
    }

    def testZipBy {
        val l = new MyResource
        val r = new MyResource
        l.take(5).zip(r.take(8)).map2(_ + _).take(3).start
        l.gen(3); r.gen(3)
        l.gen(3); r.gen(3)
        assertFalse(l.closed)
        assertFalse(r.closed)
        l.gen(3); r.gen(3)
        assertTrue(l.closed)
        assertTrue(r.closed)
    }

}
