

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class DropWhile[A](_1: Seq[A], _2: A => Boolean) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        var go = false
        _1 _for { x =>
            if (!go && !_2(x)) {
                go = true
            }
            if (go) {
                f(x)
            }
        } _andThen {
            k
        }
    }
}
