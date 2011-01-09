

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


    private class Loop(_1: Context) extends Resource[Unit] {
        @volatile private[this] var isActive = true
        override def context = _1
        override protected def closeResource() = isActive = false
        override protected def openResource(f: Reaction[Unit]) {
            context `for` { _ =>
                while (isActive) {
                    f()
                }
            } exit {
                case Exit.End => f.exit(Exit.Closed)
                case q => f.exit(q)
            }
        }
    }
}


/**
 * Context is one-element sequence of the Unit.
 */
trait Context extends Seq[Unit] {

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

    /**
     * Turns into an infinite sequence of the Units.
     */
    final def loop: Seq[Unit] = new Context.Loop(this)

    @Annotation.equivalentTo("foreach(_ => body)")
    final def eval(body: => Unit): Unit = foreach(_ => body)

    /**
     * Prefers act to self; avoid ShiftToSelf if possible.
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
