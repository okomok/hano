

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Race[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override val context = _1.context upper _2.context

    override def forloop(f: Reaction[A]) {
        var winner: Seq[A] = null
        def winnerSet(xs: Seq[A]) {
            if (winner eq null) {
                winner = xs
            }
        }
        def winnerIs(xs: Seq[A]) = (winner ne null) && (winner eq xs)

        val _enter = new MergeEnter(f, context)
        var _p1, _p2 = Exit.Empty.asExit

        _1.shift {
            context
        } onEnter { p =>
            _p1 = p
            _enter(p)

            if (winnerIs(_1)) {
                _p2(Exit.Failure(break.Control))
            }
        } onEach { x =>
            f.beforeExit {
                winnerSet(_1)

                if (winnerIs(_1)) {
                    _p2(Exit.Failure(break.Control))
                    f(x)
                }
            }
        } onExit { q =>
            f.beforeExit {
                winnerSet(_2)

                if (winnerIs(_1)) {
                    f.exit(q)
                }
            }
        } start()

        _2.shift {
            context
        } onEnter { p =>
            _p2 = p
            _enter(p)

            if (winnerIs(_2)) {
                _p1(Exit.Failure(break.Control))
            }
        } onEach { x =>
            f.beforeExit {
                winnerSet(_2)

                if (winnerIs(_2)) {
                    _p1(Exit.Failure(break.Control))
                    f(x)
                }
            }
        } onExit { q =>
            f.beforeExit {
                winnerSet(_1)

                if (winnerIs(_2)) {
                    f.exit(q)
                }
            }
        } start()
    }
}
