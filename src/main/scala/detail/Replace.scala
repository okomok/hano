

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Replace[A](_1: Seq[A], _2: util.Iter[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: Reaction[A]) {
        val it = _2.begin
        For(_1) { x =>
            if (it.hasNext) {
                f(it.next)
            } else {
                f(x)
            }
        } AndThen {
            f.onExit(_)
        }
    }
}

private[hano]
class ReplaceRegion[A](_1: Seq[A], _2: Int, _3: Int, _4: util.Iter[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: Reaction[A]) =
        _1.fork{ _.take(_2).onEach(f(_)) }.
           fork{ _.slice(_2, _3).replace(_4).onEach(f(_)) }.
           fork{ _.drop(_3).react(f) }.
           start()
}