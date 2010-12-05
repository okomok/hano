

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package utiltest


import com.github.okomok.hano.util.{Cursor, Iter}
import junit.framework.Assert._


class CursorTest extends org.scalatest.junit.JUnit3Suite {

    def testFromIterator: Unit = {
        val tr = Iter(2,4,6)
        val t = Cursor.from(tr.begin)
        assertEquals(2, t.deref)
        t.increment
        assertEquals(4, t.deref)
        t.increment
        assertEquals(6, t.deref)
        t.increment
        assertTrue(t.isEnd)
        val i = t.toIterator
        assertFalse(i.hasNext)
    }

    def testToIterator: Unit = {
        val tr = Iter(2,4,6)
        val i = Cursor.from(tr.begin).toIterator
        assertEquals(2, i.next)
        assertEquals(4, i.next)
        assertEquals(6, i.next)
        assertFalse(i.hasNext)
        val t = Cursor.from(i)
        assertTrue(t.isEnd)
    }

    def testFromJIterator: Unit = {
        val tr = new java.util.ArrayList[Int]
        tr.add(2); tr.add(4); tr.add(6)
        val t = Cursor.from(tr.iterator)
        assertEquals(2, t.deref)
        t.increment
        assertEquals(4, t.deref)
        t.increment
        assertEquals(6, t.deref)
        t.increment
        assertTrue(t.isEnd)
        val i = t.toIterator
        assertFalse(i.hasNext)
    }

    def testToJIterator: Unit = {
        val tr = Iter(2,4,6)
        val i = Cursor.from(tr.begin).toJIterator
        assertEquals(2, i.next)
        assertEquals(4, i.next)
        assertEquals(6, i.next)
        assertFalse(i.hasNext)
        val t = Cursor.from(i)
        assertTrue(t.isEnd)
    }

    def testToIter: Unit = {
        val tr = Iter(2,4,6)
        val v = Iter.from(Cursor.from(tr.begin))
        expect(Iter(2,4,6))(v)
    }

/*
    def testFusion: Unit = {
        val tr = Iter(2,4,6)
        val i = iterator.fromIterator(tr.begin.toIterator).toIterator
        assertEquals(2, i.next)
        //println("fusion")
        val t1 = iterator.fromIterator(i)
        assertEquals(4, t.deref1)
        t1.++
        assertEquals(6, t.deref1)
        val i1 = t1.toIterator
        assertEquals(6, i1.next)
        //println("fusion")
        val t2 = iterator.fromIterator(i1)
        assertFalse(t2)
    }
*/
}
