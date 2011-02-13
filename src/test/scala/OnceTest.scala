

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class OnceTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val xs = hano.async.loop.generate(0 until 5).once

        expect(hano.Iter.from(0 until 5))(xs.toIter)

        intercept[hano.SeqOnce.MultipleForloopException[_]] {
            xs.start
        }
    }
}
