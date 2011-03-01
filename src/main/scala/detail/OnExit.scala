

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnExit[A](_1: Seq[A], _2: Exit.Status => Unit) extends SeqProxy[A] {
    override val self = _1.react(Reaction(_ => (), _ => (), _2))
}


private[hano]
class OnSuccess[A](_1: Seq[A], _2: () => Unit) extends SeqProxy[A] {
    override val self = _1.onExit {
        case Exit.Success => _2()
        case _ => ()
    }
}

private[hano]
class OnFailure[A](_1: Seq[A], _2: Throwable => Unit) extends SeqProxy[A] {
    override val self = _1.onExit {
        case Exit.Failure(t) => _2(t)
        case _ => ()
    }
}
