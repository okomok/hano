

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Tail[A](_1: Seq[A]) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        var first = true
        _1 onEach { x =>
            if (first) {
                first = false
            } else {
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
