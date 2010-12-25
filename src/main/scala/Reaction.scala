

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Triggered by Seq.forloop
 */
trait Reaction[-A] {

    /**
     * Reacts on each element.
     */
    def apply(x: A): Unit

    /**
     * Reacts on the exit.
     */
    def onExit(q: Exit): Unit

}


object Reaction {

    def apply[A](f: A => Unit, k: Exit => Unit) = new Reaction[A] {
        override def apply(x: A) = f(x)
        override def onExit(q: Exit) = k(q)
    }

/*
    @Annotation.returnThat
    def from[A](that: Reaction[A]): Reaction[A] = that

    @Annotation.conversion
    implicit def fromFunction[A](f: A => Unit): Reaction[A] = new Reaction[A] {
        override def apply(x: A) = f(x)
        override def onExit(q: Exit) = ()
    }
*/
}
