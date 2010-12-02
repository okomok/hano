

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


/**
 * Runs in constructor-call-site.
 */
case class Strict[R](_1: ByName[R]) extends Function0[R] {
    private[this] val v = _1()
    override def apply = v
}

object Strict extends Strategy {
    override def apply[R](f: => R) = new Strict(f)
}
