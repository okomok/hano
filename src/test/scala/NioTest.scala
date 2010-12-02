

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


import java.nio.channels._


class NioTest extends org.scalatest.junit.JUnit3Suite {

    def testOff: Unit = ()

    def testTrivial(off: Int): Unit = {

        val s: Selector = null

        hano.Nio.selection(s) foreach { key =>
            if (key.isAcceptable) {
                _accept(key)
            } else if (key.isReadable) {
                _read(key)
            }
        }

        s.close
    }

    def _accept(key: SelectionKey) {
    }

    def _read(key: SelectionKey) {
    }

}
