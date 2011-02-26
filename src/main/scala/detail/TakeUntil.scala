

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    override def context =  _1.context upper _2.context

    override def forloop(f: Reaction[A]) {
        val _enter = new Entrance.Two(f)

        _2.shift {
            context
        } onEnter { p =>
            _enter(p)
            f.exit(Exit.End)
        } start()

        _1.shift {
            context
        } onEnter {
            _enter
        } onEach {
            f(_)
        } onExit {
            f.exit(_)
        } start()
    }
}
