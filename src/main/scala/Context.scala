

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Context is one-element sequence of the Unit.
 */
trait Context extends Seq[Unit] {

    @annotation.returnThis @inline
    final def asContext: Context = this

    /**
     * No effects; context shall be anytime reusable.
     */
    final override def close() = ()

    @annotation.returnThis
    final override def context = this

    /**
     * Terminates this context.
     */
    def exit: Unit = ()

    /**
     * Shall be thread-safe, and preserve order of subscription.
     */
    override def forloop(f: Reaction[Unit]): Unit

    /**
     * Evaluates a `body`.
     */
    final def eval(body: => Unit): Unit = foreach(_ => body)

    /**
     * Evaluates a `body` until the future.
     */
    final def future[R](body: => R): () => R = Val(map(_ => body)).future

    private[hano]
    final def upper(_that: => Context): Context = {
        lazy val that = _that
        // unknown <: self <: other
        if (this eq Unknown) {
            if (that eq Unknown) {
                async
            } else {
                that
            }
        } else if (this eq Self) {
            if ((that eq Self) || (that eq Unknown)) {
                this
            } else {
                that
            }
        } else {
            this
        }
    }
}
