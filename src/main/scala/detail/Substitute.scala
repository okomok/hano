

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Substitute[A](_1: Seq[A], _2: PartialFunction[Throwable, Seq[A]]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        _1.shift {
            process
        } onEnter {
            f.enter(_)
        } onEach {
            f(_)
        } onExit {
            case Exit.Failure(t) if _2.isDefinedAt(t) => {
                _2(t).shift {
                    process
                } forloop(f)
            }
            case q => f.exit(q)
        } start()
    }
}
