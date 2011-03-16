

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Timeout[A](_1: Seq[A], _2: Long) extends SeqAdapter.Of[A](_1) {
    require(_2 >= 0, "timeout duration shall be nonnegative")

    override def forloop(f: Reaction[A]) {
        val out = new java.util.concurrent.atomic.AtomicBoolean(false)
        var safe = false

        Timer.nondaemon.schedule {
            _2
        } onEach { _ =>
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
                    f.exit(Exit.Failure(new java.util.concurrent.TimeoutException))
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }
}
