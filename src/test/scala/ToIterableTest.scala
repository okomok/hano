

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ToIterableTest extends org.scalatest.junit.JUnit3Suite {

    def testSync: Unit = {
        val it = hano.Seq(0,1,2,3,4).toIterable(queue = new java.util.concurrent.SynchronousQueue[Any])
        expect(hano.Iter(0,1,2,3,4))(hano.Iter.from(it))
    }

    def testSyncTimeout: Unit = {
        val it = hano.Seq(0,1,2,3,4).toIterable(queue = new java.util.concurrent.SynchronousQueue[Any], timeout = 2000)
        expect(hano.Iter(0,1,2,3,4))(hano.Iter.from(it))
    }

    def testTo: Unit = {
        val r = hano.Seq(1,2,3,4,5,6)
        assertEquals(hano.Iter(1,2,3,4,5,6), r.toIter)
    }

    def testLong: Unit = {
        val r = hano.from(0 until 400)
        assertEquals(hano.Iter.from(0 until 400), r.toIter)
    }

    def testTakeStoppable: Unit = {
        val r = hano.from(Stream.from(0))
        val t = r.take(4)
        expect(hano.Iter(0,1,2,3))(t.toIter)
    }

    def testExit: Unit = {
        var exited = false
        val r = hano.from(0 until 3).onExit{_ => exited = true}
        r.start
        assert(exited)
    }

    /* close is NOW exit.
    def testCloseIsNotEnd: Unit = {
        val r = hano.from(Stream.from(0)).onExit{_ => fail("shall be no end")}
        val t = r.take(4)
        expect(hano.Iter(0,1,2,3))(t.toIter)
    }
    */

    def testReForeach: Unit = {
        val r = hano.from(Stream.from(0))
        locally {
            val t = r.take(4)
            expect(hano.Iter(0,1,2,3))(t.toIter)
        }
        locally {
            val t = r.take(4)
            expect(hano.Iter(0,1,2,3))(t.toIter)
        }
    }

    def testToList {
        val xs = hano.async.pull(0 until 5)
        expect(List(0,1,2,3,4))(xs.toList)
    }
}
