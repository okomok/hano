

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class React[A](_1: Seq[A], _2: A => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        For(_1) { x =>
            _2(x)
            f(x)
        } AndThen {
            k
        }
    }
}

private class ReactMatch[A](_1: Seq[A], _2: PartialFunction[A, Unit]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        For(_1) { x =>
            if (_2.isDefinedAt(x)) {
                _2(x)
            }
            f(x)
        } AndThen {
            k
        }
    }
}
