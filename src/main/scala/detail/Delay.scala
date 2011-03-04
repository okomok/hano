

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Delay[A](_1: Seq[A], _2: Long) extends SeqAdapter.Of[A](_1) {
    override def context = _Timer.nondaemon

    override def forloop(f: Reaction[A]) {
        val now = new Now

        def _delay(body: => Unit) {
            _Timer.nondaemon.schedule {
               now + _2
            } onEach { _ =>
                body
            } start()
        }

        _1.onEnter { p =>
            _delay {
                f.enter(p)
            }
        } onEach { x =>
            _delay {
                f(x)
            }
        } onExit { q =>
            _delay {
                f.exit(q)
            }
        } start()
    }

    override def delay(i: Long): Seq[A] = _1.delay(_2 + i) // delay.delay fusion
}
