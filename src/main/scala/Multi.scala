

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


case class Multi[A](_1: Seq[Reaction[A]]) extends Reaction[A] {
    override def apply(x: A) = for (f <- _1) f(x)
    override def onExit(q: Exit) = for (f <- _1) f.onExit(q)
}
