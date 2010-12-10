

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Tail[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        var first = true
        For(_1) { x =>
            if (first) {
                first = false
            } else {
                f(x)
            }
        } AndThen {
            k
        }
    }
}
