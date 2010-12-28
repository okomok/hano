

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._


class ResourceTest extends org.scalatest.junit.JUnit3Suite {

    class TrivialResource extends hano.NoExitResource[Int] {
        override protected def closeResource = { job = null }
        override protected def openResource(f: Int => Unit) {
            assertEquals(null, job)
            job = { _ => f(10); f(12); f(2); f(8) }
        }
        var job: Unit => Unit = null
    }

    def testTrivial {
        val r = new TrivialResource
        def doIt() {
            val out = new java.util.ArrayList[Int]
            r.foreach { x =>
                out.add(x)
            }
            r.job()
            r.close
            assertEquals(hano.Iter(10, 12, 2, 8), hano.Iter.from(out))
        }

        doIt()
    }

    def testThrown {
        val r = new TrivialResource
        def doIt() {
            val out = new java.util.ArrayList[Int]
            r.foreach { x =>
                out.add(x)
            }
            r.job()
            //r.close()
            assertEquals(hano.Iter(10, 12, 2, 8), hano.Iter.from(out))
        }

        doIt()
        intercept[hano.ResourceException[_]] {
            doIt()
        }
    }

}
