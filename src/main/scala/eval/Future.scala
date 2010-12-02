

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


/**
 * Runs (possibly) in the thread-pool.
 * If the thread-pool is full, _1 runs in the result-retrieving-site.
 */
case class Future[R](_1: ByName[R]) extends Function0[R] {
    private[this] val f = Parallel(_1, Lazy)
    override def apply = f()
}

object Future extends Strategy {
    override def apply[R](f: => R) = new Future(f)
}
