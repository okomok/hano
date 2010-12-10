

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Filter[A](_1: Seq[A], _2: A => Boolean) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        For(_1) { x =>
            if (_2(x)) {
                f(x)
            }
        } AndThen {
            k
        }
    }
}

private class Remove[A](_1: Seq[A], _2: A => Boolean) extends Forwarder[A] {
    override protected val delegate = _1.filter(!_2(_))
}
