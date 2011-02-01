

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class React[A](_1: Seq[A], _2: Reaction[A]) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        _1.forloop {
            Reaction(
                x => { _2(x); f(x) },
                q => { _2.exit(q); f.exit(q) }
            )
        }
    }

    override def react(f: Reaction[A]): Seq[A] = { // react.react fusion
        _1.react {
            Reaction(
                x => { _2(x); f(x) },
                q => { _2.exit(q); f.exit(q) }
            )
        }
    }

    override def foreach(f: A => Unit) { // react.foreach fusion
        _1.forloop {
            Reaction(
                x => { _2(x); f(x) },
                _2.exit(_)
            )
        }
    }

    override def start() { // react.start fusion
        _1.forloop {
            Reaction(
                _2(_),
                _2.exit(_)
            )
        }
    }
}
