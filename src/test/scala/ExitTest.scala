

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ExitTest extends org.scalatest.junit.JUnit3Suite {

    def testOriginClosedSent {
        val c = new java.util.concurrent.CountDownLatch(1)
        val a = new java.util.ArrayList[Int]
        for (x <- hano.async.onExit {
            case hano.Exit.Failure(hano.Exit.ByOther(hano.Exit.Success)) => c.countDown
            case _ => ()
        }.pull(0 until 10)) {
            a.add(x)
        }
        c.await
        expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
    }

    def testOriginClosedNotSent {
        val c = new java.util.concurrent.CountDownLatch(1)
        val a = new java.util.ArrayList[Int]
        for (x <- hano.async.pull(0 until 10).onExit{
            case hano.Exit.Failure(hano.break.Control) => ()
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
        for (x <- hano.from(0 until 10).onExit {
            case hano.Exit.Failure(hano.Exit.ByOther(hano.Exit.Success)) => closed = true
            case _ => ()
        }.pull(0 until 9)) {
            a.add(x)
        }
        assert(closed)
        expect(hano.Iter.from(0 until 9))(hano.Iter.from(a))
    }

    def testIterClosedNotSent {
        val a = new java.util.ArrayList[Int]
        var closed = false
        for (x <- hano.from(0 until 10).onExit{
            case hano.Exit.Failure(hano.break.Control) => closed = true
            case _ => ()
        }) {
            a.add(x)
        }
        expect(false)(closed)
        expect(hano.Iter.from(0 until 10))(hano.Iter.from(a))
    }

}

