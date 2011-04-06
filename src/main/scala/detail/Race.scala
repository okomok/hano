

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Race[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override val process = _1.process upper _2.process

    override def forloop(f: Reaction[A]) {
        var winner: Seq[A] = null
        def winnerSet(xs: Seq[A]) {
            if (winner eq null) {
                winner = xs
            }
        }
        def winnerIs(xs: Seq[A]) = (winner ne null) && (winner eq xs)

        var _p1, _p2 = Exit.Empty.asExit
        val _q = Exit.Failure(break.Control)

        def _release() {
            if (winnerIs(_1)) {
                _p2(_q)
            }
            if (winnerIs(_2)) {
                _p1(_q)
            }
        }

        _1.shift {
            process
        } onEnter { p =>
            _p1 = p
            f.enter(p)

            _release()
        } onEach { x =>
            f.beforeExit {
                winnerSet(_1)

                if (winnerIs(_1)) {
                    f(x)
                }

                _release()
            }
        } onExit { q =>
            f.beforeExit {
                winnerSet(_2)

                if (winnerIs(_1)) {
                    f.exit(q)
                }

                _release()
            }
        } start()

        _2.shift {
            process
        } onEnter { p =>
            _p2 = p
            f.enter(p)

            if (winnerIs(_2)) {
                _p1(_q)
            }
        } onEach { x =>
            f.beforeExit {
                winnerSet(_2)

                if (winnerIs(_2)) {
                    f(x)
                }

                _release()
            }
        } onExit { q =>
            f.beforeExit {
                winnerSet(_1)

                if (winnerIs(_2)) {
                    f.exit(q)
                }

                _release()
            }
        } start()
    }
}
