

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class UtilTest extends org.scalatest.junit.JUnit3Suite {

    def testDefault {
        def foo(implicit x: hano.Util.Default[Int] = -1): Int = x.value
        expect(-1)(foo)
        expect(-1)(foo())
        expect(3)(foo(3))
    }

    var _y = 0
    def newInt: Int = { _y += 1; _y }

    def testByName {
        def foo(implicit x: hano.Util.Default.ByName[Int] = newInt): Unit = {
            val f = x()
            expect(1)(f())
            expect(2)(f())
        }
        foo
    }
}
