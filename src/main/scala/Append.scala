

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Append[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: A => Unit, k: Exit => Unit) {
        For(_1) { x =>
            f(x)
        } AndThen {
            case Exit.End =>
                For(_2) { y =>
                    f(y)
                } AndThen {
                    k
                }
            case q => k(q)
        }
    }
}
