

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Breakable[A](_1: Seq[A]) extends SeqAdapter[(A, () => Unit)] {
    override protected val underlying = _1
    override def forloop(f: Reaction[(A, () => Unit)]) {
        _1 onEach { x =>
            // Note f.exit in f.apply is illegal.
            // Effect of context.eval{close()} would be too late.
            f(x, () => close())
        } onExit {
            f.exit(_)
        } start()
    }
}
