

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * A single-element sequence in Self context
 */
case class Single[A](_1: A) extends Seq[A] {
    override def context = Self
    override def forloop(f: Reaction[A]) {
        context.onEnter {
            f.enter(_)
        } onEach { _ =>
            f(_1)
        } onExit {
            f.exit(_)
        } start()
    }
}
