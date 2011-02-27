

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class PrependTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val a = hano.Iter(1,2,3,4,5)
        val xs = hano.Seq(1,2,3)
        val ys = hano.Seq(4,5)
        expect(a)((ys prepend xs).toIter)
    }
}
