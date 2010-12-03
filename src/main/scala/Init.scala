

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Init[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        var prev: Option[A] = None
        _1 _for { x =>
            if (!prev.isEmpty) {
                f(prev.get)
            }
            prev = Some(x)
        } _andThen {
            k
        }
    }
}