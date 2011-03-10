

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Duplicate[A](_1: Seq[A]) extends SeqAdapter.Of[A](_1) {
    override def forloop(f: Reaction[A]) = _forloop(f)

    private[this] var _f: Reaction[A] = null
    private[this] val _forloop = {
        IfFirst[Reaction[A]] { f =>
            _f = f
        } Else { f =>
            _1.onEnter { p =>
                val p2 = new Exit.Second(p)
                _f.enter(p2); f.enter(p2)
            } onEach { x =>
                _f(x); f(x)
            } onExit { q =>
                _f.exit(q); f.exit(q)
            } start()
        }
    }
}
