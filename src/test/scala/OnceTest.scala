

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano


import junit.framework.Assert._
import hano.Exit

/*
class OnceTest extends org.scalatest.junit.JUnit3Suite {
    def testTrivial {
        val out = new java.util.ArrayList[Int]

        class Trivial extends hano.Seq[Int] {
            override def forloop(f: hano.Reaction[Int]) {
                f(10)
                f(5)
            }
        }

        val r = new Trivial().once
        r.foreach(out.add(_))
        expect(hano.Iter(10, 5))(hano.Iter.from(out))
        intercept[hano.OnceException[_]] {
            r.start
        }
    }
}
*/
