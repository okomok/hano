

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Builds single-or-empty sequence from an expression.
 */
case class Optional[A](_1: () => A) extends SeqProxy[A] {
    override val self = Seq.from {
        try {
            Some(_1())
        } catch {
            case _ => None
        }
    }
}

object Optional {
    def apply[A](body: => A)(implicit d: DummyImplicit) = new Optional(() => body)
}
