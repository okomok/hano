

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.ArrayList


/**
 * Infinite Seq list (immutable)
 */
final class Rist[A] extends Seq[A] { self =>
    private[this] val xs = new ArrayList[A]
    private[this] val outs = new ArrayList[Reaction[A]]

    override def forloop(f: Reaction[A]) {
         for (x <- Iter.from(xs).able) {
            f(x)
        }
        outs.add(f)
    }

    def add(y: A) {
        xs.add(y)
        for (out <- Iter.from(outs).able) {
            out(y)
        }
    }
    def addAll(ys: Iter[A]) {
        for (y <- ys.able) {
            add(y)
        }
    }

    def toReaction = new Reaction[A] {
        override def apply(x: A) { self.add(x) }
        override def exit(q: Exit) = ()
    }
}


object Rist {
    /**
     * Constructs a Rist with initial values.
     */
    def apply[A](inits: A*): Rist[A] = {
        val that = new Rist[A]
        that.addAll(inits)
        that
    }
}
