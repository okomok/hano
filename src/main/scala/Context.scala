

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
    def async: Context = new detail.Async()

    /**
     * In the event-dispatch-thread
     */
    val inEdt: Context = new detail.InEdt()
}


/**
 * Context is one-element sequence of the Unit.
 */
trait Context extends Seq[Unit] with Reaction[() => Unit] {

    /**
     * No effects; context shall be reusable.
     */
    final override def close() = ()

    @Annotation.returnThis
    final override def context = this

    /**
     * Shall be thread-safe, and preserve order of subscription.
     */
    override def forloop(f: Reaction[Unit]): Unit

    /**
     * Turns into closeable infinite sequence of the Units.
     */
    final def loop: Seq[Unit] = new detail.Loop(this)

    @Annotation.equivalentTo("foreach(_ => body())")
    final override def apply(body: () => Unit): Unit = foreach(_ => body())

    @Annotation.equivalentTo("apply(() => body)")
    final def eval(body: => Unit): Unit = apply(() => body)

    /**
     * Prefers Context.async to Context.self.
     */
    private[hano]
    final def upper(that: Context): Context = {
        if (this eq Context.self) {
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
