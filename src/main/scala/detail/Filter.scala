

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Filter[A](_1: Seq[A], _2: A => Boolean) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: Reaction[A]) {
        For(_1) { x =>
            if (_2(x)) {
                f(x)
            }
        } AndThen {
            f.exit(_)
        }
    }
}

private[hano]
class Remove[A](_1: Seq[A], _2: A => Boolean) extends SeqProxy[A] {
    override val self = _1.filter(!_2(_))
}
