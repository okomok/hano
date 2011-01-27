

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
trait GeneratorCommon { self =>

    type Env[-A] = Reaction[A] with java.io.Flushable

    /**
     * Creates an Iterator from a sequence.
     */
    def iterator[A](xs: Seq[A]): Iterator[A]

    /**
     * Creates an Iterable from a body.
     */
    final def apply[A](body: Env[A] => Unit): Iterable[A] = new Apply(body)

    /**
     * Creates an Iterable from a sequence.
     */
    final def iterable[A](xs: Seq[A]): Iterable[A] = new IterableImpl(xs)

    /**
     * Converts a Traversable to Iterable.
     */
    final def traverse[A](xs: scala.collection.TraversableOnce[A]): Iterable[A] = new Traverse(xs)

    private class Apply[A](_1: Env[A] => Unit) extends Iterable[A] {
        override def iterator = self.iterator(new BodyToSeq(_1))
    }

    private class IterableImpl[A](_1: Seq[A]) extends Iterable[A] {
        override def iterator = self.iterator(_1)
    }

    private class Traverse[A](_1: scala.collection.TraversableOnce[A]) extends Iterable[A] {
        override def iterator = self.iterator(Seq.from(_1))
    }

    private class BodyToSeq[A](body: Env[A] => Unit) extends Seq[A] {
        override def context = Self
        override def forloop(f: Reaction[A]) {
            val g = new MultiExitable(f)
            try {
                body(g)
                g.exit(Exit.End)
            } catch {
                case t: Throwable => g.exitNothrow(Exit.Failed(t))
            }
        }
    }

    // Accepts multiple exit calls, though it's illegal.
    private class MultiExitable[A](f: Reaction[A]) extends Reaction[A] with java.io.Flushable {
        private[this] var exited = false
        override def flush() {
            if (f.isInstanceOf[java.io.Flushable]) {
                f.asInstanceOf[java.io.Flushable].flush()
            }
        }
        override def apply(x: A) = f(x)
        override def exit(q: Exit) {
            if (!exited) {
                exited = true
                f.exit(q)
            } else {
                q match {
                    case Exit.Failed(t) => LogErr(t, "abandoned exception in Generator")
                    case _ => ()
                }
            }
        }
    }
}
