

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class AwaitTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val out = new java.util.ArrayList[Int]
        hano.async.loop.pull {
            0 until 1000
        } onEach {
            out.add(_)
        } await()

        expect(hano.Iter.from(0 until 1000))(hano.Iter.from(out))
    }
}
