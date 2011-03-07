

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Append[A](_1: Seq[A], _2: Seq[A]) extends SeqProxy[A] {
    override val self = new AppendIf(_1, _2, {
        case Exit.Success => true
        case _ => false
    })
}


private[hano]
class AppendOnExit[A](_1: Seq[A], _2: Seq[A]) extends SeqProxy[A] {
    override val self = new AppendIf(_1, _2, {
        _ => true
    })
}


private[hano]
class AppendIf[A](_1: Seq[A], _2: Seq[A], cond: Exit.Status => Boolean) extends Seq[A] {
    override val context = _1.context upper _2.context

    override def forloop(f: Reaction[A]) {
        val _enter = new MergeEnter(f, context)

        _1.shift {
            context
        } onEnter {
            _enter(_)
        } onEach {
            f(_)
        } onExit { q =>
            f.beforeExit {
                if (cond(q)) {
                    _2.shift {
                        context
                    } onEnter {
                        _enter(_)
                    } onEach {
                        f(_)
                    } onExit {
                        f.exit(_)
                    } start()
                } else {
                    f.exit(q)
                }
            }
        } start()
    }
}
