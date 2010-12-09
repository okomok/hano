

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class TakeUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val _k = CallOnce[Exit] { q => k(q);close() }

        _2 _for { y =>
            _k(Exit.End)
        } _andThen { q =>
            _k(q)
        }

        _1 _for { x =>
            if (!_k.isDone) {
                f(x)
            } else {
                _k(Exit.End)
            }
        } _andThen { q =>
            _k(q)
        }
    }
}
