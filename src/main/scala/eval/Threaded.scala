

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


/**
 * Runs in a newly created thread.
 */
case class Threaded[R](_1: ByName[R]) extends Function0[R] {
    private[this] val f = new Invoke(_1, r => new Thread(r).start())
    override def apply = f()
}

object Threaded extends Strategy {
    override def apply[R](f: => R) = new Threaded(f)
}
