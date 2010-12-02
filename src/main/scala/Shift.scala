

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Shift[A](_1: Seq[A], _2: (=> Unit) => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        _1 _for { x =>
            _2{f(x)}
        } _then { q =>
            _2{k(q)}
        }
    }
}

private class ShiftReact[A](_1: Seq[A], _2: A => (A => Unit) => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        _1 _for { x =>
            _2(x)(f)
        } _then {
            // FIXEME how to shift k?
            k
        }
    }
}
