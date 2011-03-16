

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Adjacent2[A](_1: Seq[A]) extends SeqAdapter.Of[(A, A)](_1) {
    override def forloop(f: Reaction[(A, A)]) {
        var prev: Option[A] = None

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                if (!prev.isEmpty) {
                    f((prev.get, x))
                }
                prev = Some(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
