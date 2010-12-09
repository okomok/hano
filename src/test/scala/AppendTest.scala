

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


class AppendTest extends org.scalatest.junit.JUnit3Suite {

    def testTrivial {
        val a = hano.util.Iter(1,2,3,4,5)
        val xs = hano.Seq(1,2,3)
        val ys = hano.Seq(4,5)
        expect(hano.util.Iter(1,2,3,4,5))((xs ++ ys).toIter)
    }

    def testNotAppendedIfThrown {
        val xs = new hano.Seq[Int] {
            override def forloop(f: Int => Unit, k: hano.Exit => Unit) {
                f(1)
                f(2)
                throw new Error
            }
        }
        val ys = hano.Seq(4,5)

        val out = new java.util.ArrayList[Int]
        try {
            for (x <- xs ++ ys) {
                out.add(x)
            }
        } catch {
            case x =>
        }
        expect(hano.util.Iter(1,2))(hano.util.Iter.from(out))
    }

}
