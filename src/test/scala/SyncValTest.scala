

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class SyncValTest extends org.scalatest.junit.JUnit3Suite {

    def testThreaded {
        val v = new hano.SyncVal[Int]
        hano.Seq.origin(hano.eval.Threaded).generate(0 until 10).forloop(v)
        expect(0)(v())
    }

    def testStrict {
        val v = new hano.SyncVal[Int]
        hano.Seq.origin(hano.eval.Strict).generate(0 until 10).forloop(v)
        expect(0)(v())
    }

    def testEmpty {
        val v = new hano.SyncVal[Int]
        hano.Seq.origin(hano.eval.Threaded).generate(Seq()).forloop(v)
        intercept[NoSuchElementException] {
            v()
        }
    }

}
