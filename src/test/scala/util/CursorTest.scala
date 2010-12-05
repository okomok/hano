

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomoktest
package hanotest
package utiltest


import com.github.okomok.hano.util.{Cursor, Iter}
import junit.framework.Assert._


class CursorTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val tr = Iter(2,4,6)
        val t = Cursor.from(tr.begin)
        assertEquals(2, t.deref)
        t.increment
        assertEquals(4, t.deref)
        t.increment
        assertEquals(6, t.deref)
        t.increment
        assertTrue(t.isEnd)
        val i = t.toSIterator
        assertFalse(i.hasNext)
    }

    def testIterator: Unit = {
        val tr = Iter(2,4,6)
        val i = Cursor.from(tr.begin).toSIterator
        assertEquals(2, i.next)
        assertEquals(4, i.next)
        assertEquals(6, i.next)
        assertFalse(i.hasNext)
        val t = Cursor.from(i)
        assertTrue(t.isEnd)
    }
/*
    def testFusion: Unit = {
        val tr = Iter(2,4,6)
        val i = iterator.fromSIterator(tr.begin.toSIterator).toSIterator
        assertEquals(2, i.next)
        //println("fusion")
        val t1 = iterator.fromSIterator(i)
        assertEquals(4, t.deref1)
        t1.++
        assertEquals(6, t.deref1)
        val i1 = t1.toSIterator
        assertEquals(6, i1.next)
        //println("fusion")
        val t2 = iterator.fromSIterator(i1)
        assertFalse(t2)
    }
*/
}
