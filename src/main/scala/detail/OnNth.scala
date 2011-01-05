

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnHead[A](_1: Seq[A], _2: A => Unit) extends Seq[A] {
    override def close() = _1.close()
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        var go = true
        For(_1) { x =>
            if (go) {
                go = false
                _2(x)
            }
            f(x)
        } AndThen {
            f.exit(_)
        }
    }
}

private[hano]
class OnNth[A](_1: Seq[A], _2: Int, _3: A => Unit) extends SeqProxy[A] {
    override val self = _1.fork{ r => r.drop(_2).onHead(_3) }
}
