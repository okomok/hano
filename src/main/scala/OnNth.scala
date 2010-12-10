

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class OnHead[A](_1: Seq[A], _2: A => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        var go = true
        For(_1) { x =>
            if (go) {
                go = false
                _2(x)
            }
            f(x)
        } AndThen {
            k
        }
    }
}

private class OnNth[A](_1: Seq[A], _2: Int, _3: A => Unit) extends Forwarder[A] {
    override protected val delegate = _1.fork{ r => r.drop(_2).onHead(_3) }
}
