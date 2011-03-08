

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class UsingTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        var closed = false
        val xs = hano.async.pull(0 until 6)

        class MyError extends RuntimeException

        intercept[MyError] {
            xs.using {
                new java.io.Closeable {
                    override def close() = closed = true
                }
            } onEach { x =>
                if (x == 2) {
                    throw new MyError
                }
            } await()
        }

        expect(true)(closed)
    }
}
