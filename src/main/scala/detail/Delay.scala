

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Delay[A](_1: Seq[A], _2: Long) extends SeqProxy[A] {
    override val self = new DelayImpl(_1, _2).shift(_1.process)
    override def delay(i: Long): Seq[A] = _1.delay(_2 + i) // delay.delay fusion
}

private[hano]
class DelayImpl[A](_1: Seq[A], _2: Long) extends SeqAdapter.Of[A](_1) {
    private[this] val _timer = Timer.nondaemon

    override def process = _timer

    override def forloop(f: Reaction[A]) {
        val now = new DateNow

        def _eval(body: => Unit) {
            _timer.schedule(now + _2).onEach(_ => body).start()
        }

        _1.onEnter { p =>
            _eval {
                f.enter(p)
            }
        } onEach { x =>
            _eval {
                f(x)
            }
        } onExit { q =>
            _eval {
                f.exit(q)
            }
        } start()
    }
}
