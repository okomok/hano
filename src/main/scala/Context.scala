

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Context is one-element sequence of the Unit.
 */
trait Context extends Seq[Unit] extends java.io.Closeable {

    @annotation.returnThis @inline
    final def asContext: Context = this

    @annotation.returnThis
    final override def context = this

    /**
     * Shall be thread-safe, and preserve order of subscription.
     */
    override def forloop(f: Reaction[Unit])

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
        // Unknown <: Self <: other
        if (this eq Unknown) {
            if (that eq Unknown) {
                async // make it "known".
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

    private[hano]
    final def toKnown: Context = this upper Unknown
}
