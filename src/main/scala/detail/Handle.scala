

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class HandleEach[A](_1: Seq[A], _2: A => Boolean) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (!_2(x)) {
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}


private[hano]
class HandleExit[A](_1: Seq[A], _2: PartialFunction[Exit.Status, Unit]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f(x)
        } onExit {
            case q if _2.isDefinedAt(q) => _2(q)
            case q => f.exit(q)
        } start()
    }
}
