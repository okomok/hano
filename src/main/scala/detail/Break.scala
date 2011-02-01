

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Break[A](_1: Seq[A]) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) = _1.start()
    override def break: Seq[A] = _1.break // break.break fusion
}


private[hano]
class BreakExit[A](_1: Seq[A], _2: PartialFunction[Exit, Unit]) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        _1 onEach {
            f(_)
        } onExit { q =>
            if (_2.isDefinedAt(q)) {
                _2(q)
            } else {
                f.exit(q)
            }
        } start()
    }
}
