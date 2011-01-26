

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Context {

    /**
     * In the call-site
     */
    val self: Context = new detail.Self()

    /**
     * In the thread-pool
     */
    def act: Context = new detail.Act()

    /**
     * In the event-dispatch-thread
     */
    val inEdt: Context = new detail.InEdt()

    /**
     * Unknown
     */
    val unknown: Context = new detail.Unknown()
}


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

    /**
     * Prefers act to self; avoid ShiftToSelf if possible.
     */
    private[hano]
    final def upper(that: Context): Context = {
        if (this eq Context.unknown) {
            if (that eq Context.unknown) {
                Context.act
            } else {
                that
            }
        } else if (this eq Context.self) {
            if (that eq Context.self) {
                this
            } else {
                that
            }
        } else {
            this
        }
    }
}
