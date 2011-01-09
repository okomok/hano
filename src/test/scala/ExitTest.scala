

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ExitTest extends org.scalatest.junit.JUnit3Suite {

    def testOriginClosedSent {
        val c = new java.util.concurrent.CountDownLatch(1)
        val a = new java.util.ArrayList[Int]
        for (x <- hano.Context.act.loop.onExit {
            case hano.Exit.Closed => c.countDown
            case _ => ()
        }.generate(0 until 10)) {
            a.add(x)
        }
        c.await
        expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
    }

    def testOriginClosedNotSent {
        val c = new java.util.concurrent.CountDownLatch(1)
        val a = new java.util.ArrayList[Int]
        for (x <- hano.Context.act.loop.generate(0 until 10).onExit{
            case hano.Exit.Closed => ()
            case _ => c.countDown
        }) {
            a.add(x)
        }
        c.await
        expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
    }

    def testIterClosedSent {
        val a = new java.util.ArrayList[Int]
        var closed = false
        for (x <- hano.Seq.from(0 until 10).onExit {
            case hano.Exit.Closed => closed = true
            case _ => ()
        }.generate(0 until 10)) {
            a.add(x)
        }
        assert(closed)
        expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
    }

    def testIterClosedNotSent {
        val a = new java.util.ArrayList[Int]
        var closed = false
        for (x <- hano.Seq.from(0 until 10).onExit{
            case hano.Exit.Closed => closed = true
            case _ => ()
        }) {
            a.add(x)
        }
        expect(false)(closed)
        expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
    }

}

