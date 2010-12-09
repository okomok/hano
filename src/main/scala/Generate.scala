

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Generate[A](_1: Seq[_], _2: util.Iter[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val it = _2.begin
        val _k = CallOnce[Exit] { q => k(q);close() }
        if (!it.hasNext) {
            _k(Exit.End)
        } else {
            _1 _for { _ =>
                if (it.hasNext) {
                    f(it.next)
                    if (!it.hasNext) {
                        _k(Exit.End)
                    }
                }
            } _andThen { q =>
                _k(q)
            }
        }
    }
}
