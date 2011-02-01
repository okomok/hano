

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Filter[A](_1: Seq[A], _2: A => Boolean) extends SeqAdapter[A] {
    override protected val underlying = _1
    override def forloop(f: Reaction[A]) {
        _1 onEach { x =>
            if (_2(x)) {
                f(x)
            }
        } onExit {
            f.exit(_)
        } start()
    }
}

private[hano]
class Remove[A](_1: Seq[A], _2: A => Boolean) extends SeqProxy[A] {
    override val self = _1.filter(!_2(_))
}
