

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano

import hano.Exit

class LoopOther[A](_1: hano.Seq[A], _2: String) extends hano.Seq[A] {
    assert(_1.context ne hano.Self)
    @volatile private[this] var isActive = true
    override def close() {
        isActive = false
        _1.close()
        println(_2 + " closed")
    }
    override def context = _1.context
    override def forloop(f: hano.Reaction[A]) = synchronized {
        isActive = true
        val _k = hano.detail.ExitOnce { q => f.exit(q); close() }

        def rec() {
            println(_2 + " rec()")
            _1 onEach { x =>
                println(_2 + " onEach " + _k.isExited)
                _k.beforeExit {
                    if (isActive) {
                        println(x)
                        f(x)
                    }
                    if (!isActive) {
                        println(_2 + " closing in onEach")
                        _k(Exit.Closed)
                    }
                }
            } onExit { q =>
                println(_2 + " onExit " + q)
                q match {
                case Exit.End => {
                    if (isActive) {
                        println(_2 + " recuring in End")
                        rec()
                    } else {
                        println(_2 + " closing in End")
                        _k(Exit.Closed)
                    }
                }
                case q => ()
                /*
                case q @ Exit.Closed => {
                    if (isActive) {
                        println("closing in Closed")
                        _k(q)
                    }
                }
                case q => {
                    println("bad happened")
                    _k(q)
                }
                */
            } } start()
        }
        rec()
    }
}

class LoopTest extends org.scalatest.junit.JUnit3Suite {

    /*def testTrivial {
        val xs = hano.Self.loop.generate(1 until 4)
        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2))(xs.loop.take(11).toIter)
    }*/

    def testTrivial2 {
        val xs = new LoopOther(hano.Act(), "Units").generate(1 until 4)
        //println(xs.toIter)
        //println(xs.toIter)
        for (x <- new LoopOther(xs, "Ints").take(100)) {
            ()
        }
        Thread.sleep(1000)
//        expect(hano.Iter(1,2,3,1,2,3,1,2,3,1,2))(xs.loop.take(11).toIter)
    }
}
