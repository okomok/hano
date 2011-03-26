

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Immutable(single-forloop) infinite list
 */
final class Rist[A] extends SeqOnce[A] with java.io.Closeable {
    private[this] val _ch = new Channel[Either[Exit.Status, A]]
    private[this] val _exit = Util.once[Exit.Status] { q => _ch.write(Left(q)) }

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

    /**
     * Adds an element to the tail.
     */
    def add(x: A): this.type = {
        _ch.write(Right(x))
        this
    }

    /**
     * Adds all the elements to the tail.
     */
    def addAll(that: Seq[A]): this.type = {
        that.noSuccess.onEach {
            add(_)
        } onExit {
            exit(_)
        } start()
        this
    }

    /**
     * Ends this sequence.
     */
    def exit(q: Exit.Status = Exit.Success) = _exit(q)

    @annotation.equivalentTo("exit(Exit.Failure(t))")
    def fail(t: Throwable) = exit(Exit.Failure(t))

    /**
     * Adds elements until exit.
     */
    def assign(that: Seq[A]) = that.forloop(toReaction)

    @annotation.aliasOf("assign")
    def :=(that: Seq[A]) = that.forloop(toReaction)

    @annotation.compatibleConversion
    def toReaction: Reaction[A] = new Rist.ToReaction(this)

    @annotation.aliasOf("add")
    def +=(x: A): this.type = add(x)

    @annotation.aliasOf("addAll")
    def ++=(xs: Seq[A]): this.type = addAll(xs)
}


object Rist {

    /**
     * Creates a `Rist` with initial value.
     */
    def apply[A](that: Seq[A]): Rist[A] = {
        val xs = new Rist[A]
        xs ++= that
    }

    private class ToReaction[A](_1: Rist[A]) extends Reaction[A] {
        override def rawApply(x: A) = _1.add(x)
        override def rawExit(q: Exit.Status) = _1.exit(q)
    }
}
