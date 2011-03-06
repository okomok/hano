

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class RaceTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val xs = hano.Timer.nondaemon.schedule(400, 100).pull(Seq(0,1,2,3,4))
        val ys = hano.Timer.nondaemon.schedule(200, 100).pull(Seq(10,11,12))
        val zs = xs race ys
        expect(hano.Iter(10,11,12))(zs.toIter)
    }

    def testTrivial2 {
        val ys = hano.Timer.nondaemon.schedule(400, 100).pull(Seq(0,1,2,3,4))
        val xs = hano.Timer.nondaemon.schedule(200, 100).pull(Seq(10,11,12))
        val zs = xs race ys
        expect(hano.Iter(10,11,12))(zs.toIter)
    }

    def testEmpty {
        val xs = hano.Timer.nondaemon.schedule(400, 100).pull(Seq(0,1,2,3,4))
        val ys = hano.Timer.nondaemon.schedule(200, 100).pull(Seq())
        val zs = xs race ys
        expect(hano.Iter(0,1,2,3,4))(zs.toIter)
    }

    def testSelf {
        val xs = hano.Seq(0,1,2,3,4)
        val ys = hano.Seq(10,11,12)
        val zs = xs race ys
        expect(hano.Iter(0,1,2,3,4))(zs.toIter)
    }
}
