

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Generator {


    @Annotation.aliasOf("Reaction[A] with java.io.Flushable")
    type Env[-A] = Reaction[A] with java.io.Flushable

    /**
     * Creates an Iterable from a body using Env.
     */
    def apply[A](body: Env[A] => Unit): Iterable[A] = new Apply(body)

    /**
     * Converts a Traversable to Iterable using a thread.
     */
    def traverse[A](xs: scala.collection.TraversableOnce[A]): Iterable[A] = new Traverse(xs)


    private class Apply[A](_1: Env[A] => Unit) extends Iterable[A] {
        override def iterator = detail.BlockingGenerator(new BodyToSeq(_1))
    }

    private class Traverse[A](_1: scala.collection.TraversableOnce[A]) extends Iterable[A] {
        override def iterator = detail.BlockingGenerator(Seq.from(_1))
    }


    private class BodyToSeq[A](body: Env[A] => Unit) extends Seq[A] {
        override def context = Context.self
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
        override def flush() = f.asInstanceOf[java.io.Flushable].flush()
        override def apply(x: A) = f(x)
        override def exit(q: Exit) {
            if (!exited) {
                exited = true
                f.exit(q)
            } else {
                q match {
                    case Exit.Failed(t) => detail.LogErr(t, "abandoned exception in Generator")
                    case _ => ()
                }
            }
        }
    }

}
