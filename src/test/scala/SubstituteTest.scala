

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class SubstituteTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val xs = hano.async.loop.pull(0 until 5)
        val ys = hano.async.loop.pull(10 until 15)

        class MyError extends RuntimeException

        val zs = xs.onEach { i =>
            if (i == 3) {
                throw new MyError
            }
        } substitute {
            case _: MyError => ys
        }

        expect(hano.Iter(0,1,2,10,11,12,13,14))(zs.toIter)
    }
}
