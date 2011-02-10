

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object multi {

    def apply[A](xs: Seq[Reaction[A]]): Reaction[A] = new Impl(xs)

    private class Impl[A](_1: Seq[Reaction[A]]) extends Reaction[A] {
        override def apply(x: A) = for (f <- _1) f(x)
        override def exit(q: Exit) = for (f <- _1) f.exit(q)
    }
}
