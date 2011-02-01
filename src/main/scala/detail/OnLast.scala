

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnLast[A](_1: Seq[A], _2: Option[A] => Unit) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        var acc: Option[A] = None
        _1 onEach { x =>
            acc = Some(x)
        } onExit {
            case Exit.End => _2(acc)
            case _ => _2(None)
        } forloop(f)
    }
}
