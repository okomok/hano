

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// BROKEN: _2 shall be Iter.

private class Unsplit[A](_1: Seq[Seq[A]], _2: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        var first = true
        _1 _for { s =>
            if (first) {
                first = false
            } else {
                for (x <- _2) {
                    f(x)
                }
            }
            for (x <- s) {
                f(x)
            }
        } _andThen {
            k
        }
    }
}
