

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Duplicate[A](_1: Seq[A]) extends Seq[A] {
    private[this] var _f: Reaction[A] = null

    private[this] val _forloop = {
        IfFirst[Reaction[A]] { f =>
            _f = f
        } Else { f =>
            _1.onEnter { p =>
                val p2 = Entrance.second(p)
                _f.enter(p2); f.enter(p2)
            } onEach { x =>
                _f(x); f(x)
            } onExit { q =>
                _f.exit(q); f.exit(q)
            } start()
        }
    }

    override def context = _1.context
    override def forloop(f: Reaction[A]) = _forloop(f)
}
