

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnHead[A](_1: Seq[A], _2: Option[A] => Unit) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        var go = true
        _1 onEach { x =>
            if (go) {
                go = false
                _2(Some(x))
            }
        } onExit { _ =>
            if (go) {
                _2(None)
            }
        } forloop(f)
    }
}
