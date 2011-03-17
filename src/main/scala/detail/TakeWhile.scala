

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeWhile[A, B >: A](_1: Seq[A], _2: A => Boolean) extends SeqAdapter.Of[B](_1) {
    override def forloop(f: Reaction[B]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                if (_2(x)) {
                    f(x)
                } else {
                    f.exit()
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
