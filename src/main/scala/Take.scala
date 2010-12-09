

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Take[A](_1: Seq[A], _2: Int) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val _k = CallOnce[Exit] { q => k(q);close() }

        if (_2 == 0) {
            _k(Exit.End)
        } else {
            var c = _2
            _1 _for { x =>
                if (c != 0) {
                    f(x)
                    c -= 1
                    if (c == 0) {
                        _k(Exit.End)
                    }
                }
            } _andThen { q =>
                _k(q)
            }
        }
    }
    override def take(n: Int): Seq[A] = _1.take(java.lang.Math.min(_2, n)) // take.take fusion
}
