

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val _k = CallOnce[Exit] { q => k(q);close() }

        For(_2) { y =>
            _k(Exit.End)
        } AndThen { q =>
            _k(q)
        }

        For(_1) { x =>
            if (!_k.isDone) {
                f(x)
            } else {
                _k(Exit.End)
            }
        } AndThen { q =>
            _k(q)
        }
    }
}
