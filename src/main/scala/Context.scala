

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Context is one-element sequence of the Unit.
 */
trait Context extends Seq[Unit] {

    @Annotation.returnThis @inline
    final def asContext: Context = this

    /**
     * No effects; context shall be anytime reusable.
     */
    final override def close() = ()

    @Annotation.returnThis
    final override def context = this

    /**
     * Terminates this context.
     */
    def exit: Unit = ()

    /**
     * Shall be thread-safe, and preserve order of subscription.
     */
    override def forloop(f: Reaction[Unit]): Unit

    @Annotation.equivalentTo("foreach(_ => body)")
    final def eval(body: => Unit): Unit = foreach(_ => body)

    private[hano]
    final def upper(that: Context): Context = {
        // unknown <: self <: other
        if (this eq Unknown) {
            if (that eq Unknown) {
                Async()
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
