

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


/**
 * Runs in the result-retrieving-site.
 */
case class Lazy[R](_1: ByName[R]) extends Function0[R] {
    private[this] lazy val v = _1()
    override def apply = v
}

object Lazy extends Strategy {
    override def apply[R](f: => R) = new Lazy(f)
    implicit def _fromExpr[R](from: => R): Lazy[R] = apply(from)
}
