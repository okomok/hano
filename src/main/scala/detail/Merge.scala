

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Merge[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override val context = _1.context upper _2.context

    override def forloop(f: Reaction[A]) {
        val _enter = new MergeEnter(f, context)
        val _end = IfFirst[Unit] { _ => () } Else { _ => f.exit(Exit.Success) }

        _1.shift {
            context
        } onEnter {
            _enter(_)
        } onEach {
            f(_)
        } onExit {
            case Exit.Success => _end()
            case q => f.exit(q)
        } start()

        _2.shift {
            context
        } onEnter {
            _enter(_)
        } onEach {
            f(_)
        } onExit {
            case Exit.Success => _end()
            case q => f.exit(q)
        } start()
    }
}


private[hano]
class MergeEnter(f: Reaction[_], ctx: Context) extends (Exit => Unit) {
    private[this] var _exit1, _exit2 = Exit.Empty.asExit

    override def apply(p: Exit) {
        if (f.isExited) {
            p(Exit.Failure(break.Control))
        } else if (f.isEntered) {
            _exit2 = p
        } else {
            _exit1 = p
            f.enter {
                Exit { q =>
                    ctx.eval { // for thread-safety
                        _exit1(q)
                        _exit2(q)
                        f.exit(Exit.Failure(Exit.ByOther(q)))
                    }
                }
            }
        }
    }
}
