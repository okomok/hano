

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package utiltest


import com.github.okomok.hano.Iter
import junit.framework.Assert._


class IterTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial: Unit = {
        val t: Iter[Int] = Iter(1,2,3)
        val u = Iter(1,2,3)
        assertNotSame(t, u)
        assertTrue(t.equalsIf(u)(_ == _))
        assertEquals(t, u)
        assertEquals(3, t.length)
        assertEquals(3, u.length)
        assertEquals(t, u)
        assertEquals(3, t.length)
        assertEquals(3, u.length)
    }

    def testTrivial2: Unit = {
        val t = Iter(1,2,3)
        val u = Iter(1,2,3,4)
        AssertNotEquals(t, u)
    }

    def testTrivial3: Unit = {
        val t = Iter(1,2,3,4,5)
        val u = Iter().of[Int]
        AssertNotEquals(t, u)
    }

    def testTrivial4: Unit = {
        val t = Iter(1,2,3)
        val u = Iter(3,4,5)
        AssertNotEquals(t, u)
    }

    def testEmpty: Unit = {
        val t = Iter().of[Int]
        val u = Iter().of[Int]
        assertEquals(t, u)
        assertEquals(t, u)
    }

    def testToString: Unit = {
        val t = Iter(1,2,3)
        assertEquals("[1, 2, 3]", t.toString)
        assertEquals("[1, 2, 3]", t.toString)
        val t0 = Iter().of[Int]
        assertEquals("[]", t0.toString)
        assertEquals("[]", t0.toString)
        val t00 = Iter()
        assertEquals("[]", t00.toString)
        assertEquals("[]", t00.toString)
        val t1 = Iter(1)
        assertEquals("[1]", t1.toString)
        assertEquals("[1]", t1.toString)
    }
/*
    def testStringize: Unit = {
        val t = Iter('1','2','3')
        assertEquals("123", t.stringize)
        assertEquals("123", t.stringize)
        val t0 = Iter().of[Char]
        assertEquals("", t0.stringize)
        assertEquals("", t0.stringize)
        val t1 = Iter('1')
        assertEquals("1", t1.stringize)
        assertEquals("1", t1.stringize)
    }
*/
    def testHashCode: Unit = {
        val t = Iter(4,7,6)
        val s = Iter(4,7,6)
        val u = Iter(4,7,9)
        assertEquals(s.hashCode, t.hashCode)
        assertEquals(t.hashCode, t.hashCode)
        AssertNotEquals(u.hashCode, t.hashCode)
    }
}
