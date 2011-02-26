

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Delay[A](_1: Seq[A], _2: Long) extends SeqAdapter[A] {
    override protected val underlying = _1

    override def context = Delay.timer

    override def forloop(f: Reaction[A]) {
        val zero = new ZeroDelay

        def _delay(body: => Unit) {
            Delay.timer.schedule {
                _2 + zero()
            } onEach { _ =>
                body
            } start()
        }

        _1.onEnter { p =>
            _detaly {
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

private[hano]
object Delay {
    private val timer = new Timer(false)
}
