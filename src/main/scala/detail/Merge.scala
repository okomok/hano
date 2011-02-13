

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Merge[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override val context = _1.context upper _2.context
    override def forloop(f: Reaction[A]) {
        val _ok = IfFirst[Exit] { _ => () } Else { q => f.exit(q) }
        def _no(q: Exit) { close(); f.exit(q) }

        _1 shift {
            context
        } onEach {
            f(_)
        } onExit {
            case Exit.End => _ok(Exit.End)
            case q => _no(q)
        } start()

        _2 shift {
            context
        } onEach {
            f(_)
        } onExit {
            case Exit.End => _ok(Exit.End)
            case q => _no(q)
        } start()
    }
}
