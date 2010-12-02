

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class TakeUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: A => Unit, k: Exit => Unit) {
        @volatile var go = true
        val _k = IfFirst[Exit] { q => k(q);close() } Else { _ => () }
        _2 _for { y =>
            go = false
            _k(End)
        } _then { q =>
            _k(q)
        }
        _1 _for { x =>
            if (go) {
                f(x)
            } else {
                _k(End)
            }
        } _then { q =>
            _k(q)
        }
    }
}
