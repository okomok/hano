

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.collection.mutable.Builder


private[hano]
class Copy[A, To](_1: Seq[A], _2: () => Builder[A, To]) extends SeqAdapter.Of[To](_1) {
    override def forloop(f: Reaction[To]) {
        val b = _2()

        _1.onEnter {
            f.enter(_)
        } onEach {
            b += _
        } onExit {
            case Exit.Success => {
                f(b.result)
                f.exit()
            }
            case q => f.exit(q)
        } start()
    }
}
