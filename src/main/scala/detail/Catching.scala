

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Catching[A](_1: Seq[A], _2: PartialFunction[Throwable, Unit]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            try {
                f(x)
            } catch {
                case t: Throwable => {
                    if (_2.isDefinedAt(t)) {
                        _2(t)
                    } else {
                        throw t
                    }
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
