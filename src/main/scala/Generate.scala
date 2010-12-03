

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Generate[A](_1: Seq[_], _2: util.Iter[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val it = _2.begin
        val _k = IfFirst[Exit] { q => k(q);close() } Else { _ => () }
        if (!it.hasNext) {
            _k(End)
        } else {
            _1 _for { _ =>
                if (it.hasNext) {
                    f(it.next)
                    if (!it.hasNext) {
                        _k(End)
                    }
                }
            } _andThen { q =>
                _k(q)
            }
        }
    }
}
