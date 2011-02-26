

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ZipWithIndex[A](_1: Seq[A]) extends SeqAdapter.Of[(A, Int)](_1) {
    override def forloop(f: Reaction[(A, Int)]) {
        var i = 0

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f(x, i)
            i += 1
        } onExit {
            f.exit(_)
        } start()
    }
}
