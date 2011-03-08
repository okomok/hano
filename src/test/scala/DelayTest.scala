

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest

import com.github.okomok.hano


class DelayTezt { // extends org.scalatest.junit.JUnit3Suite {

    class MySeq extends hano.Seq[Int] {
        override def process = hano.Self
        override def forloop(f: hano.Reaction[Int]) {
            f.enter()
            println("sleeping0")
            Thread.sleep(1000)
            f(0)
            f(1)
            f(2)
            println("sleeping1")
            Thread.sleep(4000)
            f(3)
            f(4)
            f(5)
            println("sleeping2")
            Thread.sleep(2000)
            f(6)
            f(7)
            f(8)
            f.exit()
        }
    }

    def testTrivial {
        val xs = new MySeq().onEach(println(_)).delay(10000)
        for (x <- xs) {
            println(x)
        }
    }
}
