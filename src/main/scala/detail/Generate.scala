

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Generate[A](_1: Seq[_], _2: util.Iter[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val _k = CallOnce[Exit] { q => k(q);close() }

        val it = _2.begin
        if (!it.hasNext) {
            _k(Exit.End)
        } else {
            For(_1) { _ =>
                if (it.hasNext) {
                    f(it.next)
                    if (!it.hasNext) {
                        _k(Exit.End)
                    }
                }
            } AndThen { q =>
                _k(q)
            }
        }
    }
}
