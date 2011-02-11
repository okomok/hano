

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Take[A](_1: Seq[A], _2: Int) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        val _k = ExitOnce { q => close(); f.exit(q) }

        if (_2 == 0) {
            context eval {
                _k(Exit.End)
            }
        } else {
            var c = _2
            _1 onEach { x =>
                if (c != 0) {
                    f(x)
                    c -= 1
                    if (c == 0) {
                        _k(Exit.End)
                    }
                }
            } onExit {
                _k(_)
            } start()
        }
    }
    override def take(n: Int): Seq[A] = _1.take(java.lang.Math.min(_2, n)) // take.take fusion
}
