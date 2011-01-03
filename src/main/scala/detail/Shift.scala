

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Shift[A](_1: Seq[A], _2: => Seq[Unit]) extends SeqProxy[A] {
    override val self = _1.shiftBy(Context.eval(_2))
}

private[hano]
class ShiftBy[A](_1: Seq[A], _2: (=> Unit) => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: Reaction[A]) {
        For(_1) { x =>
            _2{f(x)}
        } AndThen { q =>
            _2{f.exit(q)}
        }
    }
}
