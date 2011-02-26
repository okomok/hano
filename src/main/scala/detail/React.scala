

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class React[A](_1: Seq[A], _2: () => Reaction[A]) extends SeqAdapter.Class[A](_1) {
    override def forloop(f: Reaction[A]) {
        val _g = _2()

        _1.forloop {
            Reaction(
                p => { _g.enter(p); f.enter(p) },
                x => { _g(x); f(x) },
                q => { _g.exit(q); f.exit(q) }
            )
        }
    }

    override def react(f: => Reaction[A]): Seq[A] = { // react.react fusion
        _1.react {
            val _f = f
            val _g = _2()

            Reaction(
                p => { _g.enter(p); _f.enter(p) },
                x => { _g(x); _f(x) },
                q => { _g.exit(q); _f.exit(q) }
            )
        }
    }

    override def foreach(f: A => Unit) { // react.foreach fusion
        val _g = _2()

        _1.forloop {
            Reaction(
                _g.enter(_),
                x => { _g(x); f(x) },
                _g.exit(_)
            )
        }
    }

    override def start() { // react.start fusion
        val _g = _2()

        _1.forloop {
            Reaction(
                _g.enter(_),
                _g(_),
                _g.exit(_)
            )
        }
    }
}
