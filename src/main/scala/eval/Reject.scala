

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


case class RejectedException(_1: String) extends RuntimeException(_1)


case class Reject[R](_1: ByName[R]) extends Function0[R] {
    throw new RejectedException("evaluation rejected")
    override def apply = throw new Error("unreachable")
}

object Reject extends Strategy {
    override def apply[R](f: => R) = new Reject(f)
}
