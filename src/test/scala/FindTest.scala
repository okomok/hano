

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest


import com.github.okomok.hano



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
