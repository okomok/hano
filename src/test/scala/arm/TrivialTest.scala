

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package armtest


import com.github.okomok.hano


import junit.framework.Assert._


class TrivialTest extends org.scalatest.junit.JUnit3Suite {

    def testFor {
        val r = TrivialResource("res")
        assertFalse(r.began)
        assertFalse(r.ended)
        for (s <- r) {
            assertEquals(s, "res")
        }
        assertTrue(r.began)
        assertTrue(r.ended)
    }
/* rejected
    def testUsing {
        val r = TrivialResource("res")
        assertFalse(r.began)
        assertFalse(r.ended)
        hano.using(r){ s =>
            assertEquals(s, "res")
        }
        assertTrue(r.began)
        assertTrue(r.ended)
    }
*/

    def testThrow {
        val r = TrivialResource("res")
        var thrown = false
        assertFalse(r.ended)
        assertFalse(thrown)
        try {
            r foreach { s =>
                assertEquals("res", s)
                throw new Error("wow")
            }
        } catch {
            case _: Error => thrown = true
        }
        assertTrue(thrown)
        assertTrue(r.ended)
    }

}
