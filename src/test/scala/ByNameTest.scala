

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class ByNameTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        def foo(b: Boolean) = hano.byName(if (b) hano.Seq(1,2,3) else hano.Seq(4,5,6))
        expect(hano.Unknown)(foo(true).context)
        expect(hano.Iter(1,2,3))(foo(true).toIter)
        expect(hano.Iter(4,5,6))(foo(false).toIter)
    }

    def testShiftFusion {
        def foo(b: Boolean) = hano.byName(if (b) hano.Seq(1,2,3) else hano.Seq(4,5,6)).shift(hano.Self)
        expect(hano.Self)(foo(true).context)
        expect(hano.Iter(1,2,3))(foo(true).toIter)
        expect(hano.Iter(4,5,6))(foo(false).toIter)
    }

}
