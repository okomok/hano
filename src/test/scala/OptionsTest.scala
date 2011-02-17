

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class OptionsTest extends org.scalatest.junit.JUnit3Suite {

    def testNth {
        val xs = hano.async.loop.pull(Seq(3,1,8,6,7,4,2,9))
        val nth = hano.Val(xs.nth(6).options)
        expect(2)(nth().get)
    }

    def testNoNth {
        val xs = hano.async.loop.pull(Seq(3,1,8,6,7,4,2,9))
        val nth = new hano.Val[Option[Int]]
        nth := xs.nth(10).options
        expect(None)(nth())
    }
}
