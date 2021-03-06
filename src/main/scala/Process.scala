

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

    final override def forloop(f: Reaction[Unit]) = head.cycle.forloop(f)

    @annotation.equivalentTo("head.onEach(_ => body).start()")
    final def invoke(body: => Unit) = head.foreach(_ => body)

    @annotation.equivalentTo("head.onEach(_ => body).await()")
    final def invokeAndWait(body: => Unit) = head.onEach(_ => body).await()

    /**
     * Returns a `Val` to evaluate `body`.
     */
    @annotation.equivalentTo("Val(head.map(_ => body))")
    final def future[R](body: => R): Val[R] = Val(head.map(_ => body))

    @annotation.optimization
    final override def head: Seq[Unit] = new Process.Head(this)

    @annotation.optimization
    final override def last: Seq[Unit] = new Process.Last(this)

    private[hano]
    final def upper(_that: => Process): Process = {
        lazy val res = _that
        // Unknown <: Self <: other
        if (this eq Unknown) {
            if (res eq Unknown) {
                async // make it "known".
            } else {
                res
            }
        } else if (this eq Self) {
            if ((res eq Self) || (res eq Unknown)) {
                this
            } else {
                res
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

    private class Last(_1: Process) extends SeqProxy[Unit] {
        override val self = _1.head
    }
}
