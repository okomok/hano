

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// SHOULD BE REVISED.

private[hano]
class FillTime(_1: Seq[_], _2: Long) extends SeqAdapter.Of[Unit](_1) {
    private[this] val _timer = Timer.daemon

    override def process = _timer

    override def forloop(f: Reaction[Unit]) {
        var u: Seq[Unit] = null

        def _fill() {
            u = _timer.schedule(_2, _2)
            u onEach { _ =>
                f()
            } start()
        }

        _fill()

        _1.onEnter {
            f.enter(_)
        } onEach { _ =>
            _fill()
        } onExit { q =>
            Timer.daemon.invoke {
                f.exit(q)
            }
        } start()
    }

    override def fillTime(i: Long): Seq[Unit] = _1.fillTime(java.lang.Math.min(_2, i)) // fillTime.fillTime fusion
}
