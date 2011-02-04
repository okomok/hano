

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import java.util.ArrayDeque
import java.util.concurrent.CountDownLatch


class ParallelSuite(poolSize: Int) {

    private val tasks = new ArrayDeque[() => Unit]
    private val barrier = new java.util.concurrent.CyclicBarrier(poolSize)

    def add(task: => Unit) {
        tasks.offer { () =>
            task
        }
    }

    def add(count: Int)(body: => Unit) {
        for (i <- 0 until count) {
            add(body)
        }
    }

    def start() {
        val done = new CountDownLatch(poolSize)

        val threads = distribute(done)
        val it = threads.iterator
        while (it.hasNext) {
            it.next.start()
        }

        done.await()
    }

    // See: Concrete Mathematics section-3.4
    private def distribute(done: CountDownLatch): ArrayDeque[Thread] = {
        var n = tasks.size
        var m = poolSize
        val that = new ArrayDeque[Thread](m)

        while (m > 0) {
            val c = java.lang.Math.ceil(n/m).asInstanceOf[Int]
            //println(n, m, c)

            var f: () => Unit = () => ()
            for (i <- 0 until c) {
                f = compose(f, compose(yld, tasks.poll()))
            }

            that.offer {
                new Thread {
                    override def run() {
                        barrier.await()
                        try {
                            f()
                        } finally {
                            done.countDown()
                        }
                    }
                }
            }
            n = n - c
            m = m - 1
        }

        assert(poolSize == that.size)
        that
    }

    private def compose(f: () => Unit, g: () => Unit) = () => { f(); g() }
    private def yld() { Thread.`yield`() }
}


class ParallelSuiteTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val suite = new ParallelSuite(5)
        var c = new java.util.concurrent.atomic.AtomicInteger(0)
        for (i <- 1 to 37) {
            suite.add {
                //println((Thread.currentThread, i))
                c.incrementAndGet
                ()
            }
        }
        suite.start
        expect(37)(c.get)
    }

    def testNotEnough {
        val suite = new ParallelSuite(5)
        var c = new java.util.concurrent.atomic.AtomicInteger(0)
        for (i <- 1 to 3) {
            suite.add {
                //println((Thread.currentThread, i))
                c.incrementAndGet
                ()
            }
        }
        suite.start
        expect(3)(c.get)
    }
}
