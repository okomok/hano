

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import junit.framework.Assert._


class ReactTest extends org.scalatest.junit.JUnit3Suite {

    def testOrder {
        val a = hano.Seq(1,2,3)
        val out = new java.util.ArrayList[Int]
        a react {
            hano.Reaction(_ => (), x => out.add(x*10), _ => out.add(99))
        } react {
            hano.Reaction(_ => (), x => out.add(x*100), _ => out.add(100))
        } start;
        assertEquals(hano.Iter(10,100,20,200,30,300,99,100), hano.Iter.from(out))
    }

    def testFusion {
        val a = hano.Seq(1,2,3)
        val out = new java.util.ArrayList[Int]
        a react {
            hano.Reaction(_ => (), x => out.add(x*10), _ => out.add(99))
        } foreach { x =>
            out.add(x*100)
        }
        assertEquals(hano.Iter(10,100,20,200,30,300,99), hano.Iter.from(out))
    }

    def testTrivial {
        val a = hano.Seq(1,2,3,2,5)
        val out = new java.util.ArrayList[Int]
        a onEachMatch {
            case 2 => out.add(20)
            case 3 => out.add(30)
        } start;
        assertEquals(hano.Iter(20,30,20), hano.Iter.from(out))
    }

    def testTotal {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a onEachMatch {
            case x => out.add(x)
        } start;
        assertEquals(hano.Iter(1,2,3,4,5), hano.Iter.from(out))
    }

    def testTotal2 {
        val a = hano.Seq(1,2,3,4,5)
        val out = new java.util.ArrayList[Int]
        a onEach {
            x => out.add(x)
        } take {
            3
        } start;
        assertEquals(hano.Iter(1,2,3/*,4,5 (now close work well.*/), hano.Iter.from(out))
    }

}
