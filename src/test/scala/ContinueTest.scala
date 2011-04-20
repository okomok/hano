

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class ContinueTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial: Unit = {
        val xs = hano.Seq.from(0 until 6)

        var out: List[Int] = Nil
        var i = 0
        for (x <- xs) {
            if (i == 3) {
                i += 1
                hano.continue
            }
            out :+= x
            i += 1
        }
        expect(List(0,1,2,4,5))(out)
    }
}
