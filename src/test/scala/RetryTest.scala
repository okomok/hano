

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class RetryTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivialSelf {
        val xs = hano.Self.pull(1 until 4).retry(4)
        expect(hano.Iter(1,2,3))(xs.toIter)
    }

    def testTrivialAsync {
        val xs = hano.async.pull(1 until 4).retry(4)
        expect(hano.Iter(1,2,3))(xs.toIter)
    }

    class MyError extends RuntimeException

    def testAsync {
        val xs = hano.async.pull(0 until 5)

        def ys(n: Int) = {
            var i = 0
            xs.onEnter { _ =>
                i += 1
            } onEach { x =>
                //println(i)
                if (x == 3) {
                    if (i == 1) {
                        //println("throw")
                        throw new MyError
                    }
                }
            } retry(n)
        }

        intercept[MyError] {
            ys(0).await()
        }

        expect(hano.Iter(0,1,2,0,1,2,3,4))(ys(1).toIter)
        expect(hano.Iter(0,1,2,0,1,2,3,4))(ys(2).toIter)
        expect(hano.Iter(0,1,2,0,1,2,3,4))(ys(10).toIter)
    }
}
