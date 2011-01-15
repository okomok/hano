

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: http://d.hatena.ne.jp/shomah4a/20110105


import scala.actors.Actor


object Generator {

    @Annotation.aliasOf("Reaction")
    type Env[-A] = Reaction[A]

    /**
     * Creates an Iterable from a body using Env.
     */
    def apply[A](body: Env[A] => Unit): Iterable[A] = new SeqToIterable(new BodyToSeq(body))

    /**
     * Converts a Traversable to Iterable using a thread.
     */
    def traverse[A](xs: scala.collection.TraversableOnce[A]): Iterable[A] = apply { * =>
        xs.foreach(*(_))
        *.end()
    }

    private[hano]
    class SeqToIterable[A](_1: Seq[A]) extends Iterable[A] {
        override def iterator: Iterator[A] = new IteratorImpl(_1)
    }

    private case class Element[A](x: A)

    private class ReactionImpl[A](a: Actor) extends Reaction[A] {
        override def apply(x: A) {
            a ! Element(x)
        }
        override def exit(q: Exit) {
            a ! q
        }
    }

    private class IteratorImpl[A](xs: Seq[A]) extends AbstractIterator[A] {
        private[this] var v: Option[A] = None
        private[this] val a = Actor.self
        Actor.actor {
            xs.forloop(new ReactionImpl[A](a))
        }
        ready()

        override protected def isEnd = v.isEmpty
        override protected def deref = v.get
        override protected def increment() = ready()

        private def ready() {
            var s: Throwable = null
            v = a.receive {
                case Element(x) => Some(x.asInstanceOf[A])
                case Exit.Failed(t) => s = t; None
                case _ => None
            }
            if (s != null) {
                throw s
            }
        }
    }

    private class BodyToSeq[A](body: Env[A] => Unit) extends Seq[A] {
        override def context = Context.self
        override def forloop(f: Reaction[A]) {
            val g = new detail.MultiExitReaction(f)
            try {
                body(g)
                g.exit(Exit.End)
            } catch {
                case t: Throwable => g.exitNothrow(Exit.Failed(t))
            }
        }
    }
}
