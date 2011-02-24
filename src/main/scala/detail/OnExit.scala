

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnExit[A](_1: Seq[A], _2: Exit => Unit) extends SeqProxy[A] {
    override val self = _1.react(Reaction(_ => (), _2))
}


private[hano]
class OnEnd[A](_1: Seq[A], _2: () => Unit) extends SeqProxy[A] {
    override val self = _1.onExit {
        case Exit.End => _2()
        case _ => ()
    }
}

private[hano]
class OnFailed[A](_1: Seq[A], _2: Throwable => Unit) extends SeqProxy[A] {
    override val self = _1.onExit {
        case Exit.Failed(t) => _2(t)
        case _ => ()
    }
}

private[hano]
class OnClosed[A](_1: Seq[A], _2: () => Unit) extends SeqProxy[A] {
    override val self = _1.onExit {
        case Exit.Closed => _2()
        case _ => ()
    }
}
