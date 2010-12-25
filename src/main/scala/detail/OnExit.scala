

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnExit[A](_1: Seq[A], _2: Exit => Unit) extends Forwarder[A] {
    override protected val delegate = _1.react(Reaction(_ => (), _2))
}
