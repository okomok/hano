

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


// See: http://d.hatena.ne.jp/shomah4a/20110105


import scala.actors.Actor


private[hano]
object ActorGenerator {

    def apply[A](xs: Seq[A]): Iterator[A] = new IteratorImpl(xs)

    private case class Element[A](x: A)

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

    private class ReactionImpl[A](a: Actor) extends Reaction[A] {
        override def apply(x: A) {
            a ! Element(x)
        }
        override def exit(q: Exit) {
            a ! q
        }
    }
}
