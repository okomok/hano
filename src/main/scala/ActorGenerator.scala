

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: http://d.hatena.ne.jp/shomah4a/20110105


import scala.actors.Actor


object ActorGenerator extends detail.GeneratorCommon {

    override def iterator[A](xs: Seq[A]): Iterator[A] = new IteratorImpl(xs)

    private class IteratorImpl[A](xs: Seq[A]) extends detail.AbstractIterator[A] {

        private case class Mail[B](msg: B)

        private[this] var v: Option[A] = None
        private[this] val a = Actor.self

        Actor.actor {
            xs.forloop(new ReactionImpl)
        }
        ready()

        override protected def isEnd = v.isEmpty
        override protected def deref = v.get
        override protected def increment() = ready()

        private def ready() {
            var s: Throwable = null
            v = a.receive {
                case Mail(Exit.Failed(t)) => s = t; None
                case Mail(q: Exit) => None
                case Mail(x) => Some(x.asInstanceOf[A])
            }
            if (s != null) {
                throw s
            }
        }

        private class ReactionImpl extends Reaction[A] {
            override def apply(x: A) {
                a ! Mail(x)
            }
            override def exit(q: Exit) {
                a ! Mail(q)
            }
        }
    }
}
