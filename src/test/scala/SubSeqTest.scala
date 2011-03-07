

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class SubseqTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial = {
        val xs = hano.async.loop.pull(Seq(1,2,3,4,5)).subseq(Seq(0,2,4,6,7))
        expect(hano.Iter(1,3,5))(xs.toIter)
    }

    def testTrivial2 = {
        val xs = hano.async.loop.pull(Seq('1','2','3','4','5')).subseq(Seq(0,1,2,3,4,5,6,7))
        expect(hano.Iter('1','2','3','4','5'))(xs.toIter)
    }

    def testFromNon0 = {
        val xs = hano.async.loop.pull(Seq('1','2','3','4','5')).subseq(Seq(1,2,3,4,5,6,7))
        expect(hano.Iter('2','3','4','5'))(xs.toIter)
    }

    def testShort = {
        val xs = hano.async.loop.pull(Seq(1,2,3,4,5)).subseq(Seq(0,2))
        expect(hano.Iter(1,3))(xs.toIter)
    }

    def testEmpty = {
        val xs = hano.async.loop.pull(Seq()).subseq(Seq(0,2,4,6,7))
        expect(hano.Iter())(xs.toIter)
    }

}
