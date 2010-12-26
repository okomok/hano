

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class BreakOutTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val xs = hano.Seq(1,2,3,4,5)
        val v: scala.collection.immutable.Vector[Int] = xs.breakOut
        expect(hano.Iter(1,2,3,4,5))(hano.Iter.from(v))
    }

}
