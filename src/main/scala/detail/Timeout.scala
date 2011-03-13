

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Timeout[A](_1: Seq[A], _2: Within) extends SeqProxy[A] {
    override val self = _2 match {
        case Within.Inf => _1
        case _ => new TimeoutElapse(_1, _2)
    }
}


private[hano]
class TimeoutElapse[A](_1: Seq[A], _2: Within) extends SeqAdapter.Of[A](_1) {
    assert(_2 != Within.Inf)

    override def forloop(f: Reaction[A]) {
        val out = new java.util.concurrent.atomic.AtomicBoolean(false)
        var safe = false

        Timer.nondaemon.schedule {
            _2.toMillis
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
