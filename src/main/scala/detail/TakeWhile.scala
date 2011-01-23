

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeWhile[A, B >: A](_1: Seq[A], _2: A => Boolean) extends Seq[B] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[B]) {
        val _k = ExitOnce { q => f.exit(q); close() }

        _1 onEach { x =>
            _k.beforeExit {
                if (_2(x)) {
                    f(x)
                } else {
                    _k(Exit.End)
                }
            }
        } onExit {
            _k(_)
        } start()
    }
}
