

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


object Impl {
/*
    import hano._

    class LoopOther[A](_1: Seq[A]) extends SeqResource[A] {
        assert(_1.context ne Self)

        private[this] var isActive = false
        override def context = _1.context
        override def closeResource() { isActive = false; _1.close() }
        override def openResource(f: Reaction[A]) {
            require(!isActive)

            isActive = true
            def _k(q: Exit) { close(); f.exit(q) }

            def rec() {
                _1 onEach { x =>
                    synchronized { // is needed because...
                        f(x) // may reenter `forloop` from other threads.
                        if (!isActive) {
                            _k(Exit.Closed)
                            // `isActive` is out of play for me.
                        }
                    }
                    // Now you can reenter `forloop`.
                } onExit { q =>
                    q match {
                        case Exit.End => {
                            if (isActive) {
                                rec()
                            }
                        }
                        case q => _k(q)
                    }
                } start()
            }
            rec()
        }
    }
*/
}


class FindTest extends org.scalatest.junit.JUnit3Suite {

     def testAssign {
         for (i <- 0 until 1000) {

             val xs = hano.async.loop.pull(0 until 90)

             // Recall a `Seq` algorithm returns a single-element `Seq`.
             val x: hano.Seq[Int] = xs find { x => x == 70 }

             locally {
                 val v = new hano.Val[Int]
                 v := x // assign
                 expect(70)(v())
             }

             // The above expression is equivalent to...
             locally {
                 val v = hano.Val(x)
                 expect(70)(v())
             }
         }
     }
}
