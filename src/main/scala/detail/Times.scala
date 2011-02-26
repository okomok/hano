

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Times[A](_1: Seq[A], _2: Int) extends SeqAdapter.Class[A](_1) {
    override def forloop(f: Reaction[A]) {
        var _n = _2

        def pred: Boolean = {
            val go = _n != 0
            _n -= 1
            go
        }

        _1.loopWhile(pred).forloop(f)
    }
}
