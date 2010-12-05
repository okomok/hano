

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import java.util.concurrent.CyclicBarrier
import java.util.ArrayList
import hano.Exit
import hano.util.Iter


class IntSenders(data: Iter[Int]*) {
    private val barrier = new CyclicBarrier(data.length + 1)
    private val senders: ArrayList[IntSender] = {
        val buf = new ArrayList[IntSender]
        for (datum <- data) {
            val s = new IntSender(datum, barrier)
            buf.add(s)
        }
        buf
    }

    def apply(n: Int): IntSender = senders.get(n)

    def activate: Unit = barrier.await

    def shutdown(f: => Unit) = {
        barrier.await
        f
    }
}


class IntSender(datum: Iter[Int], barrier: CyclicBarrier) extends hano.Seq[Int] {
    override def forloop(f: Int => Unit, k: Exit => Unit) = {
        new Thread {
            override def run = {
                barrier.await

                for (i <- datum) {
                    f(i)
                    Thread.`yield`()
                    //Thread.sleep(100)
                }

                barrier.await
            }
        }.start
    }
}


class IntReceiver(expected: Iter[Int]) extends Function1[Int, Unit] {
    import junit.framework.Assert._

    private val buf = new ArrayList[Int]

    override def apply(e: Int) = synchronized { buf.add(e) }

    def assertMe = {
        val ys = buf.clone.asInstanceOf[ArrayList[Int]]
        java.util.Collections.sort(ys, implicitly[Ordering[Int]])
        assertTrue("expected: " + expected + " actural: " + hano.util.Iter.from(buf), expected == hano.util.Iter.from(ys))
    }
}
