

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Replace[A](_1: Seq[A], _2: util.Iter[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val it = _2.begin
        _1 _for { x =>
            if (it.hasNext) {
                f(it.next)
            } else {
                f(x)
            }
        } _andThen {
            k
        }
    }
}

private class ReplaceRegion[A](_1: Seq[A], _2: Int, _3: Int, _4: util.Iter[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) =
        _1.fork{ _.take(_2).react(f) }.
           fork{ _.slice(_2, _3).replace(_4).react(f) }.
           fork{ _.drop(_3).react(f).onExit(k) }.
           start()
}
