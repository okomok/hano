

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeWhile[A, B >: A](_1: Seq[A], _2: A => Boolean) extends Seq[B] {
    override def close() = _1.close()
    override def forloop(f: Reaction[B]) {
        val _k = CallOnce[Exit] { q => f.onExit(q);close() }

        For(_1) { x =>
            if(!_k.isDone) {
                if (_2(x)) {
                    f(x)
                } else {
                    _k(Exit.End)
                }
            }
        } AndThen { q =>
            _k(q)
        }
    }
}