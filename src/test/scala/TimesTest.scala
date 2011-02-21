

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class TimesTest extends org.scalatest.junit.JUnit3Suite {

    def teztTrivialSelf {
        val xs = hano.Self.loop.pull(1 until 4).times(4)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2,3))(xs.toIter)
    }

    def testTrivialAsync {
        val xs = hano.async.loop.pull(1 until 4).times(4)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2,3))(xs.toIter)
    }
}
