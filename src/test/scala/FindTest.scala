

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


object Impl {

    import hano._

    class LoopOther[A](_1: Seq[A], _2: Int = 1) extends SeqResource[A] {
        assert(_1.context ne Self)

        private[this] var isActive = true
        private[this] var c = new java.util.concurrent.CountDownLatch(1)
        c.countDown()
        override def context = _1.context
        override def closeResource() {
            isActive = false
            _1.close()
        }
        override def openResource(f: Reaction[A]) {
            println("awaiting")
            c.await()
            println("pass")
            c = new java.util.concurrent.CountDownLatch(1)

            isActive = true
            def _k(q: Exit) {
                f beforeExit {
                close()
                f.exit(q)
                println("countdown")
                c.countDown()
                }
            }

            def rec() {
                _1 onEach { x =>
                   // synchronized {
                    f beforeExit {
                        for (i <- 0 until _2) {
                            if (isActive) {
                                f(x)
                            }
                        }
                        if (!isActive) {
                            _k(Exit.Closed)
                        }
                    }
                   // }
                } onExit {// q =>
                   // synchronized { q match {
                    case Exit.End => {
                        if (isActive) {
                            rec()
                        } else {
                            _k(Exit.Closed)
                        }
                    }
                    case q @ Exit.Closed => {
                        if (isActive) {
                            _k(q)
                        }
                    }
                    case q => _k(q)
                   // } }
                } start()
            }
            rec()
        }
    }
}


class FindTest extends org.scalatest.junit.JUnit3Suite {

     def testAssign {
         for (i <- 0 until 100) {

             val xs = hano.async.loop.pull(0 until 9)

             // Recall a `Seq` algorithm returns a single-element `Seq`.
             val x: hano.Seq[Int] = xs find { x => x == 7 }

             locally {
                 val v = new hano.Val[Int]
                 v := x // assign
                 expect(7)(v())
             }

             // The above expression is equivalent to...
             locally {
                 val v = hano.Val(x)
                 expect(7)(v())
             }

         }
     }
}
