

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class OptionalTest extends org.scalatest.junit.JUnit3Suite {

    def testOption {
        hano.block { Y =>
            import Y._
            val k = each(hano.optional(12))
            expect(12)(k)
        }
    }

    def testOptionEmpty {
        hano.block { Y =>
            import Y._
            val k = each(hano.optional(throw new Error("doh")))
            throw new Error
        }
    }
}
