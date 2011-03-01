

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class ModificationTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val f = new hano.Reaction[Int] {
            override def rawEnter(p: hano.Exit) = ()
            override def rawApply(x: Int) = Thread.sleep(10)
            override def rawExit(q: hano.Exit.Status) = ()
        }

        val suite = new ParallelSuite(10)

        @volatile var thrown = false
        suite.add(50) {
            try {
                f.enter()
                f(10)
                f.exit()
            } catch {
                case _: java.util.ConcurrentModificationException => thrown = true
            }
        }
        suite.start()
        Thread.sleep(1000)
        assert(thrown)
    }
}
