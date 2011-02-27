

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Merge[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override val context = _1.context upper _2.context

    override def forloop(f: Reaction[A]) {
        val _enter = new Entrance.Two(f)
        val _end = IfFirst[Unit] { _ =>
            ()
        } Else { q =>
            f.exit(Exit.End)
        }

        _1.shift {
            context
        } onEnter {
            _enter
        } onEach {
            f(_)
        } onExit {
            case Exit.End => _end()
            case q => f.exit(q)
        } start()

        _2.shift {
            context
        } onEnter {
            _enter
        } onEach {
            f(_)
        } onExit {
            case Exit.End => _end()
            case q => f.exit(q)
        } start()
    }
}
