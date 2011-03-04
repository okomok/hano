

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class FillTime(_1: Seq[_], _2: Long) extends SeqAdapter.Of[Unit](_1) {
    override def context = _Timer.daemon

    override def forloop(f: Reaction[Unit]) {
        var u: Seq[Unit] = null

        def _fill() {
            u = _Timer.daemon.schedule(_2, _2)
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
            _Timer.daemon.eval {
                f.exit(q)
            }
        } start()
    }

    override def fillTime(i: Long): Seq[Unit] = _1.fillTime(java.lang.Math.min(_2, i)) // fillTime.fillTime fusion
}
