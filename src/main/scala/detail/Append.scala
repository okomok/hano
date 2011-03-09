

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Append[A](_1: Seq[A], _2: Seq[A]) extends SeqProxy[A] {
    override val self = new AppendIf(_1, _2, _.isSuccess)
}


private[hano]
class AppendOnExit[A](_1: Seq[A], _2: Seq[A]) extends SeqProxy[A] {
    override val self = new AppendIf(_1, _2, _ => true)
}


private[hano]
class AppendIf[A](_1: Seq[A], _2: Seq[A], cond: Exit.Status => Boolean) extends Seq[A] {
    override val process = _1.process upper _2.process

    override def forloop(f: Reaction[A]) {
        _1.shift {
            process
        } onEnter {
            f.enter(_)
        } onEach {
            f(_)
        } onExit { q =>
            f.beforeExit {
                if (cond(q)) {
                    _2.shift {
                        process
                    } forloop(f)
                } else {
                    f.exit(q)
                }
            }
        } start()
    }
}
