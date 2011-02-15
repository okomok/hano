

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def context =  _1.context upper _2.context
    override def forloop(f: Reaction[A]) {
        def _k(q: Exit) { close(); f.exit(q) }
        var go = true

        _2 shift {
            context
        } onEach { _ =>
            go = false
            _k(Exit.End)
        } start()

        _1 shift {
            context
        } onEach { x =>
            f beforeExit {
                if (go) {
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
