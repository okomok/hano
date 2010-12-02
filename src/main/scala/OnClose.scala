

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class OnClose[A](_1: Seq[A], _2: eval.ByName[Unit]) extends Seq[A] {
    override def close() = { _2(); _1.close() }
    override def forloop(f: A => Unit, k: Exit => Unit) = _1.forloop(f, k)
}
