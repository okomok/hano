

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class IteratorsTest extends org.scalatest.junit.JUnit3Suite {

    def testUnfold {
        val it = hano.Iterators.unfold(10)(b => if (b == 0) None else Some(b.toString, b-1))
        expect(hano.Iter("10","9","8","7","6","5","4","3","2","1"))(hano.Iter.from(it))
    }

    def testLoop {
        val it = hano.Iterators.loop(Iterator(1,2,3)).take(10)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1))(hano.Iter.from(it))
    }

    def testRepeatWhile {
        var i = 0
        val it = hano.Iterators.repeatWhile(Iterator(1,2,3)){i += 1; i != 3}
        expect(hano.Iter(1,2,3,1,2,3))(hano.Iter.from(it))
    }

    def testRepeat {
        val it = hano.Iterators.repeat(Iterator(1,2,3), 3)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3))(hano.Iter.from(it))
    }
}
