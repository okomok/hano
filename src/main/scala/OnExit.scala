

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class OnExit[A](_1: Seq[A], _2: Exit => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        _1 _for { x =>
            f(x)
        } _then { q =>
            _2(q)
            k(q)
        }
    }
}
