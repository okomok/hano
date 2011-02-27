

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Append[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override val context = _1.context upper _2.context

    override def forloop(f: Reaction[A]) {
        val _enter = new Entrance.Two(f)

        _1.shift {
            context
        } onEnter {
            _enter
        } onEach {
            f(_)
        } onExit {
            case Exit.End => {
                _2.shift {
                    context
                } onEnter {
                    _enter
                } onEach {
                    f(_)
                } onExit {
                    f.exit(_)
                } start()
            }
            case q => f.exit(q)
        } start()
    }
}
