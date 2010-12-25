

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Shift[A](_1: Seq[A], _2: (=> Unit) => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        For(_1) { x =>
            _2{f(x)}
        } AndThen { q =>
            _2{k(q)}
        }
    }
}

private[hano]
class ShiftReact[A](_1: Seq[A], _2: A => (A => Unit) => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        For(_1) { x =>
            _2(x)(f)
        } AndThen {
            // FIXEME how to shift k?
            k
        }
    }
}
