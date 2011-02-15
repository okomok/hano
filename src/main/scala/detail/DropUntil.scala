

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class DropUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        @volatile var go = false

        _2 onEach { _ =>
            go = true
            _2.close()
        } start()

        _1 onEach { x =>
            if (go) {
                _2.close()
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
