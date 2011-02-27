

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Take[A](_1: Seq[A], _2: Int) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) {
        var c = _2

        _1.onEnter { p =>
            f.enter(p)
            if (c == 0) {
                f.exit(Exit.End)
            }
        } onEach { x =>
            f.beforeExit {
                f(x)
                c -= 1
                if (c == 0) {
                    f.exit(Exit.End)
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }

    override def take(n: Int): Seq[A] = _1.take(java.lang.Math.min(_2, n)) // take.take fusion
}
