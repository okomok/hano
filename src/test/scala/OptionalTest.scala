

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class OptionalTest extends org.scalatest.junit.JUnit3Suite {

    def testOption {
        hano.Block { Y =>
            import Y._
            val k = each(hano.Optional(12))
            expect(12)(k)
        }
    }

    def testOptionEmpty {
        hano.Block { Y =>
            import Y._
            val k = each(hano.Optional(throw new Error("doh")))
            throw new Error
        }
    }
}
