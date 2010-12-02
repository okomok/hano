

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Breakable[A](_1: Seq[A]) extends Seq[Tuple2[A, Function0[Unit]]] {
    override def close() = _1.close()
    override def forloop(f: Tuple2[A, Function0[Unit]] => Unit, k: Exit => Unit) {
        val _k = IfFirst[Exit] { q => k(q);close() } Else { _ => () }
        _1 _for { x =>
            f(x, () => _k(End))
        } _andThen { q =>
            _k(q)
        }
    }
}
