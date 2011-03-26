

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class CancelTest extends org.scalatest.junit.JUnit3Suite {

    def testTrival {
        val cancel = new hano.Cancel
        var out: List[Int] = Nil
        hano.async.pull(0 until 1000).onEnter(cancel).onEach { x =>
            out :+= x
        } start()

        Thread.sleep(100)
        cancel()
        assert(out.size < 1000)
    }

    def testInReaction {
        val cancel = new hano.Cancel
        var i = 0
        hano.async.pull(0 until 1000).onEnter(cancel).onEach { x =>
            if (i == 50) {
                cancel()
            }
            i += 1
        } start()

        Thread.sleep(1000)
        expect(51)(i)
    }

    def testBefore {
        val cancel = new hano.Cancel
        cancel()
        var i = 0
        hano.async.pull(0 until 1000).onEnter(cancel).onEach { x =>
            i += 1
        } start()

        Thread.sleep(1000)
        expect(0)(i)
    }
}

