

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class ListenTest extends org.scalatest.junit.JUnit3Suite {

    val t = new java.util.Timer(true)

    def naturals: hano.Seq[Int] = {
        val xs = hano.listen[Unit]() { * =>
            val l = new java.util.TimerTask {
                override def run() {
                    *()
                }
            }
            *.add {
                t.schedule(l, 0, 100)
            }
            *.remove{
                l.cancel()
            }
        }
        xs.pull(Stream.iterate(0)(_ + 1))
    }

    def testNaturals {
        expect(hano.Iter(0,1,2,3,4,5))(naturals.take(6).toIter)
    }

    def testLoopable {
        val xs: hano.Seq[Int] = naturals.take(3).cycle.take(7)
        expect(hano.Iter(0,1,2,0,1,2,0))(xs.toIter)
    }
}
