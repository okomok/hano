

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import junit.framework.Assert._


class ReactTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val a = hano.Seq(1,2,3,2,5)
        val out = new java.util.ArrayList[Int]
        a reactMatch {
            case 2 => out.add(20)
            case 3 => out.add(30)
        } start;
        assertEquals(hano.util.Iterable(20,30,20), hano.util.Iterable.from(out))
    }

    def testTotal {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a reactMatch {
            case x => out.add(x)
        } start;
        assertEquals(hano.util.Iterable(1,2,3,4,5), hano.util.Iterable.from(out))
    }


    def testTotal2 {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a react {
            x => out.add(x)
        } take {
            3
        } start;
        assertEquals(hano.util.Iterable(1,2,3,4,5), hano.util.Iterable.from(out))
    }

}
