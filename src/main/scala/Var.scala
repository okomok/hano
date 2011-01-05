

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Infinite sequence of variables, considered as a lightweight `Rist`.
 * This can hold only one listener. You can't place this in nested
 * position of for-expression if outer sequence has multiple elements.
 */
final class Var[A](private[this] var x: Option[A] = None) extends Seq[A] { self =>
    def this(x: A) = this(Some(x))

    @volatile private[this] var out: Reaction[A] = null

    override def context = Context.async

    override def forloop(f: Reaction[A]) {
        if (!x.isEmpty) f(x.get)
        out = f
    }

    def :=(y: A) {
        x = Some(y)
        out(y)
    }

    def toReaction = new Reaction[A] {
        override def apply(x: A) { self := x }
        override def exit(q: Exit) = ()
    }
}


object Var {
    @Annotation.equivalentTo("new Var(x)")
    def apply[A](x: A): Var[A] = new Var(x)

    @Annotation.equivalentTo("new Var[A]")
    def apply[A]: Var[A] = new Var[A]
}
