

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class OptionalTest extends org.scalatest.junit.JUnit3Suite {

    def testOptional {
        hano.cps {
            val k = hano.optional(12).!
            expect(12)(k)
        }
    }

    def testOptionEmpty {
        hano.cps {
            val k = hano.optional(throw new Error("doh")).!
            throw new Error
        }
    }
}
