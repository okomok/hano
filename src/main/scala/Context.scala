

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Context is one-element sequence of the Unit.
 */
trait Context extends Seq[Unit] with Reaction[() => Unit] {
    final override def close() = ()
    final override def context = this

    /**
     * Shall be thread-safe.
     * Moreover all the Reaction method calls shall be serialized.
     */
    def forloop(f: Reaction[Unit]): Unit

    final def loop: Seq[Unit] = new detail.Loop(this)

    final override def apply(body: () => Unit): Unit = foreach(_ => body())
    final def eval(body: => Unit): Unit = apply(() => body)

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
