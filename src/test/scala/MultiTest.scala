

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano
import hano.Reaction


import junit.framework.Assert._


class MultiTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val out = new java.util.ArrayList[Int]
        val rs = new java.util.ArrayList[Reaction[Int]]
        rs.add(Reaction(_ => (), x => out.add(x+10), _ => ()))
        rs.add(Reaction(_ => (), x => out.add(x+20), _ => ()))
        val t = hano.Seq(4,5,1,3)
        val k_ = t.forloop(hano.multi(rs))
        assertEquals(hano.Iter(14,24,15,25,11,21,13,23), hano.Iter.from(out))
    }

    def testReactive {
        val out = new java.util.ArrayList[Int]
        val rs = new java.util.ArrayList[Reaction[Int]]
        rs.add(Reaction(_ => (), x => out.add(x+10), x => ()))
        rs.add(Reaction(_ => (), x => out.add(x+20), x => ()))
        val t = hano.Seq(4,5,1,3)
        val k_ = t.forloop(hano.multi(hano.from(hano.Iter.from(rs))))
        assertEquals(hano.Iter(14,24,15,25,11,21,13,23), hano.Iter.from(out))
    }

}
