

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import java.util.concurrent.CyclicBarrier
import java.util.ArrayList
import hano.Exit


class IntSenders(_data: scala.collection.immutable.IndexedSeq[Int]*) {
    private val data: scala.collection.immutable.IndexedSeq[scala.collection.immutable.IndexedSeq[Int]] = hano.util.Iterable.from(_data.toArray[scala.collection.immutable.IndexedSeq[Int]])
    private val barrier = new CyclicBarrier(data.size + 1)
    private val senders: scala.collection.immutable.IndexedSeq[IntSender] = {
        val buf = new ArrayList[IntSender]
        for (datum <- data) {
            val s = new IntSender(datum, barrier)
            buf.add(s)
        }
        hano.util.Iterable.from(buf)
    }

    def apply(n: Int): IntSender = senders.nth(n)

    def activate: Unit = barrier.await

    def shutdown(f: => Unit) = {
        barrier.await
        f
    }
}


class IntSender(datum: scala.collection.immutable.IndexedSeq[Int], barrier: CyclicBarrier) extends hano.Seq[Int] {
    override def forloop(f: Int => Unit, k: Exit => Unit) = {
        new Thread {
            override def run = {
                barrier.await

                for (i <- datum) {
                    f(i)
                    //Thread.sleep(100)
                }

                barrier.await
            }
        }.start
    }
}


class IntReceiver(expected: scala.collection.immutable.IndexedSeq[Int]) extends Function1[Int, Unit] {
    import junit.framework.Assert._

    private val buf = new ArrayList[Int]

    override def apply(e: Int) = synchronized { buf.add(e) }

    def assertMe = {
        assertTrue("expected: " + expected + " actural: " + hano.util.Iterable.from(buf), expected == hano.util.Iterable.from(buf).sort)
    }
}
