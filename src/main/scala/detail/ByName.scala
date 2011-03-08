

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ByName[A](_1: () => Seq[A]) extends Seq[A] {
    override def context = Unknown
    override def forloop(f: Reaction[A]) = _1().forloop(f)

    override def shift(that: Seq[_]): Seq[A] = new ByName.Shift(_1, that) // byName.shift fusion
}


object ByName {

    private class Shift[A](_1: () => Seq[A], _2: Seq[_]) extends Seq[A] {
        override def context = _2.context
        override def forloop(f: Reaction[A]) = _1().shift(_2).forloop(f)
    }
}
