

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Timeout[A](_1: Seq[A], _2: Seq[_]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        val out = new java.util.concurrent.atomic.AtomicBoolean(false)
        var safe = false

        _2.head.onEach { _ =>
            out.set(true)
        } start()

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f.beforeExit {
                if (safe) {
                    f(x)
                } else if (out.compareAndSet(false, true)) {
                    safe = true
                    f(x)
                } else {
                    f.fail(new java.util.concurrent.TimeoutException)
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
