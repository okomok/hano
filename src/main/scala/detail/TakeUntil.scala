

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override val process =  _1.process upper _2.process

    override def forloop(f: Reaction[A]) {
        _2.shift {
            process
        } onEnter {
            f.enter(_)
        } onEach { _ =>
            f.exit(Exit.Success)
        } start()

        _1.shift {
            process
        } onEnter {
            f.enter(_)
        } onEach { x =>
            f(x)
        } onExit {
            f.exit(_)
        } start()
    }
}
