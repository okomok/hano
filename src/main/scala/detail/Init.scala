

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Init[A](_1: Seq[A]) extends SeqAdapter[A] {
    override protected val underlying = _1

    override def forloop(f: Reaction[A]) {
        var prev: Option[A] = None
        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            if (!prev.isEmpty) {
                f(prev.get)
            }
            prev = Some(x)
        } onExit {
            f.exit(_)
        } start()
    }
}
