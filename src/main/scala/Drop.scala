

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Drop[A](_1: Seq[A], _2: Int) extends Seq[A] {
    Pre.nonnegative(_2, "drop")

    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        var c = _2
        _1 _for { x =>
            if (c == 0) {
                f(x)
            } else {
                c -= 1
            }
        } _andThen {
            k
        }
    }

    override def drop(n: Int): Seq[A] = _1.drop(_2 + n) // drop.drop fusion
}
