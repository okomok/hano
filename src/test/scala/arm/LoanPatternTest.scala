

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package armtest


// See:
//   http://stackoverflow.com/questions/2207425/what-automatic-resource-management-alternatives-exists-for-scala


object using { import scala.util.continuations._
    object Env { def apply[A <: {def close()}](x: A): A @suspendable = shift(k => try{k(x)}finally{x.close}) }
    def apply[A](ctx: Env.type => A @cpsParam[A, Unit]) { reset(ctx(Env)) }
}

class LoanPatternTest extends org.scalatest.junit.JUnit3Suite {

    def testScope { import java.io._
        using { * =>
            val reader = *(new BufferedReader(new FileReader("loan-pattern.in")))
            val writer = *(new BufferedWriter(new FileWriter("loan-pattern.out")))
            var line = reader.readLine
            while (line != null) {
                writer write line
                writer.newLine
                line = reader.readLine
            }
        }
    }

}
