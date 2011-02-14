

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class OptionalTest extends org.scalatest.junit.JUnit3Suite {

    def testNth {
        val xs = hano.async.loop.generate(Seq(3,1,8,6,7,4,2,9))
        val nth = hano.Val(xs.nth(6).optional)
        expect(2)(nth().get)
    }

    def testNoNth {
        val xs = hano.async.loop.generate(Seq(3,1,8,6,7,4,2,9))
        val nth = new hano.Val[Option[Int]]
        nth := xs.nth(10).optional
        expect(None)(nth())
    }

    /*
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

    */
}
