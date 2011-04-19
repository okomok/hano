

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override val process = _1.process upper _2.process

    override def forloop(f: Reaction[A]) {
        _2.shift {
            process
        } onEnter {
            f.enter(_)
        } onEach { _ =>
            f.exit() // right now, but process is shifted.
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

private[hano]
class TakeUntil_[A](_1: Seq[A], _2: Seq[_]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        @volatile var go = true

        _2.head.onEach { _ =>
            go = false
        } start()

        _1.onEnter { p =>
            f.enter(p)
            if (!go) {
                f.exit()
            }
        } onEach { x =>
            if (!go) {
                f.exit() // keeps process, but delayed
            }
            f(x)
        } onExit {
            f.exit(_)
        } start()
    }
}
