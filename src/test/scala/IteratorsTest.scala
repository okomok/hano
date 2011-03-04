

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class IteratorsTest extends org.scalatest.junit.JUnit3Suite {

    def testUnfold {
        val it = hano.Iterators.unfold(10)(b => if (b == 0) None else Some(b.toString, b-1))
        expect(hano.Iter("10","9","8","7","6","5","4","3","2","1"))(hano.Iter.from(it))
    }

    def testCycle {
        val it = hano.Iterators.cycle(Iterator(1,2,3)).take(10)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1))(hano.Iter.from(it))
    }
}
