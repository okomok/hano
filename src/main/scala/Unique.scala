

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Unique[A](_1: Seq[A]) extends Forwarder[A] {
    override protected val delegate = _1.uniqueBy(_ == _)
    override def unique: Seq[A] = this // unique.unique fusion
}

private class UniqueBy[A](_1: Seq[A], _2: (A, A) => Boolean) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        var prev: Option[A] = None
        _1 _for { x =>
            if (prev.isEmpty || !_2(prev.get, x)) {
                f(x)
            }
            prev = Some(x)
        } _then {
            k
        }
    }
}
