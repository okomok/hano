

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Merge[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override val process = _1.process upper _2.process

    override def forloop(f: Reaction[A]) {
        val _end = IfFirst[Unit] { _ => () } Else { _ => f.exit(Exit.Success) }

        _1.shift {
            process
        } onEnter {
            f.enter(_)
        } onEach {
            f(_)
        } onExit {
            case Exit.Success => _end()
            case q => f.exit(q)
        } start()

        _2.shift {
            process
        } onEnter {
            f.enter(_)
        } onEach {
            f(_)
        } onExit {
            case Exit.Success => _end()
            case q => f.exit(q)
        } start()
    }
}
