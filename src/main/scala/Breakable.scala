

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Breakable[A](_1: Seq[A]) extends Seq[Tuple2[A, Function0[Unit]]] {
    override def close() = _1.close()
    override def forloop(f: Tuple2[A, Function0[Unit]] => Unit, k: Exit => Unit) {
        val _k = CallOnce[Exit] { q => k(q);close() }
        _1 _for { x =>
            f(x, () => _k(Exit.End))
        } _andThen { q =>
            _k(q)
        }
    }
}
