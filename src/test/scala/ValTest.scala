

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano
import org.testng.annotations._


class ValTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val v1 = new hano.Val[Int]

        val suite = new ParallelSuite(10)
        suite.add(50) {
            v1 := 12
        }
        suite.add(50) {
            @volatile var called = false
            for (v <- v1) {
                if (called) {
                    fail("multiple call impossible")
                }
                expect(12)(v)
                called = true
            }
        }
        suite.start()
    }

    def testTrivialAsync {
        val v1 = new hano.Val[Int](hano.Context.async)

        val suite = new ParallelSuite(10)
        suite.add(50) {
            v1 := 12
        }
        suite.add(50) {
            @volatile var called = false
            for (v <- v1) {
                if (called) {
                    fail("multiple call impossible")
                }
                expect(12)(v)
                called = true
            }
        }
        suite.start()
    }

    def testThrows {
        val v = new hano.Val[Int]
        v := 11
        v := 11
        intercept[hano.MultipleAssignmentException[_]] {
            v := 12
        }
    }
}
