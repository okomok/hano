

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class DropUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: A => Unit, k: Exit => Unit) {
        @volatile var go = false
        val g = eval.Lazy{_2.close()}

        for (y <- _2) {
            go = true
            g()
        }

        _1 _for { x =>
            if (go) {
                g()
                f(x)
            }
        } _andThen {
            k
        }
    }
}