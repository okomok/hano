

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Fork[A](_1: Seq[A], _2: Seq[A] => Unit) extends SeqProxy[A] {
    override lazy val self = {
        val (xs, ys) = _1.duplicate
        val _xs = new Idempotent(xs)
        _2(_xs)
        _xs.start() // guarantees a `forloop` call
        ys
    }
}


private[hano]
class Idempotent[A](_1: Seq[A]) extends SeqAdapter[A] {
    private[this] var go = true
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        if (go) {
            go = false
            _1.forloop(f)
        }
    }
}
