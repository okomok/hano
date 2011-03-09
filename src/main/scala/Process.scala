

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Process is an infinite sequence of the `Unit`s.
 */
trait Process extends Seq[Unit] with java.io.Closeable {
    @annotation.returnThis @inline
    final def asProcess: Process = this

    @annotation.returnThis
    final override def process = this

    /**
     * Evaluates a `body`.
     * This shall preserve order of subscription.
     */
    @annotation.threadSafe
    def `do`(f: Reaction[Unit])

    final override def forloop(f: Reaction[Unit]) = single.cycle.forloop(f)

    final def eval(body: => Unit) = single.foreach(_ => body)

    final def single: Seq[Unit] = new Process.Single(this)

    /**
     * Evaluates a `body` until the future.
     */
    final def future[R](body: => R): () => R = Val(single.map(_ => body)).future

    private[hano]
    final def upper(_that: => Process): Process = {
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
    final def toKnown: Process = this upper Unknown
}


private[hano]
object Process {

    private class Single(_1: Process) extends Seq[Unit] {
        override def process = _1
        override def forloop(f: Reaction[Unit]) = _1.`do`(f)
    }
}
