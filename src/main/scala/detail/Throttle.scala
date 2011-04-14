

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Throttle[A](_1: Seq[A], _2: Long) extends SeqProxy[A] {
    override val self = new ThrottleImpl(_1, _2).shift(_1.process)
}

private[hano]
class ThrottleImpl[A](_1: Seq[A], _2: Long) extends SeqAdapter.Of[A](_1) {
    private[this] val _timer = Timer.nondaemon

    override def process = _timer

    override def forloop(f: Reaction[A]) {
        def _eval(time: java.util.Date)(body: => Unit): Cancel = {
            val res = new Cancel
            _timer.schedule(time).onEnter(res).onEach(_ => body).start()
            res
        }

        var c = new Cancel
        val now = new DateNow

        _1.onEnter { p =>
            _eval(now()) {
                f.enter(p)
            }
        } onEach { x =>
            f.beforeExit {
                c()
                c = _eval(now + _2) {
                    f(x)
                }
            }
        } onExit { q =>
            f.beforeExit {
                _eval(now + _2) {
                    f.exit(q)
                }
            }
        } start()
    }
}
