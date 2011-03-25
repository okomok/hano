

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable(single-forloop) infinite list
 */
final class Rist[A] extends SeqOnce[A] with java.io.Closeable {
    private[this] val _ch = new Channel[Either[Exit.Status, A]]

    override def close() = _ch.close()
    override def process = _ch.process

    override protected def forloopOnce(f: Reaction[A]) {
        _ch.cycle.onEnter {
            f.enter(_)
        } onEach { lr =>
            f.beforeExit {
                lr match {
                    case Left(q) => f.exit(q)
                    case Right(x) => f(x)
                }
            }
        } onExit {
            f.exit(_)
        } start()
    }

    def add(x: A) {
        _ch.write(Right(x))
    }

    def exit(q: Exit.Status) {
        _ch.write(Left(q))
    }

    /**
     * Adds elements until exit.
     */
    def assign(that: Seq[A]) = that.forloop(toReaction)

    @annotation.aliasOf("assign")
    def :=(that: Seq[A]) = that.forloop(toReaction)

    @annotation.compatibleConversion
    def toReaction: Reaction[A] = new Rist.ToReaction(this)

    @annotation.aliasOf("add")
    def +=(x: A) = add(x)
}


object Rist {

    /**
     * Creates a `Rist` with initial value.
     */
    def apply[A](that: Seq[A]): Rist[A] = {
        val xs = new Rist[A]
        xs := that
        xs
    }

    private class ToReaction[A](_1: Rist[A]) extends Reaction[A] {
        override def rawApply(x: A) = _1.add(x)
        override def rawExit(q: Exit.Status) = _1.exit(q)
    }
}
