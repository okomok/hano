

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Retry[A](_1: Seq[A], _2: Int) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        val pred = new TrueUntil(_2 + 1)
        new LoopWhileIf(_1, pred, !_.isSuccess).forloop(f)
    }
}
