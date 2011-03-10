

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
     * Evaluates `f` one-time.
     * This shall preserve order of subscription.
     */
    @annotation.threadSafe
    def `do`(f: Reaction[Unit])

    @annotation.optimization
    override def head: Seq[Unit] = new Process.Head(this)

    final override def forloop(f: Reaction[Unit]) = head.cycle.forloop(f)

    @annotation.equivalentTo("head.onEach(_ => body).start()")
    final def invoke(body: => Unit) = head.foreach(_ => body)

    @annotation.equivalentTo("head.onEach(_ => body).await()")
    final def invokeAndWait(body: => Unit) = head.onEach(_ => body).await()

    /**
     * Evaluates a `body` until the future.
     */
    final def future[R](body: => R): () => R = new detail.HeadFuture(map(_ => body))

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

    private class Head(_1: Process) extends Seq[Unit] with detail.SingleSeq[Unit] {
        override def process = _1
        override def forloop(f: Reaction[Unit]) = _1.`do`(f)
    }
}
