

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


// See: http://d.hatena.ne.jp/shomah4a/20110105


object ActorGenerator extends detail.GeneratorCommon {

    override def iterator[A](xs: Seq[A]): Iterator[A] = new IteratorImpl(xs)

    private class IteratorImpl[A](xs: Seq[A]) extends detail.AbstractIterator[A] {

        private[this] case class Msg(msg: Any)

        private[this] var v: Option[A] = None
        private[this] val ch = new Channel[Msg]

        actors.Actor.actor {
            xs.forloop(new ReactionImpl)
        }
        ready()

        override protected def isEnd = v.isEmpty
        override protected def deref = v.get
        override protected def increment() = ready()

        private def ready() {
            v = ch.receive {
                case Msg(Exit.Failed(t)) => throw t
                case Msg(q: Exit) => None
                case Msg(x) => Some(x.asInstanceOf[A])
            }
        }

        private class ReactionImpl extends Reaction[A] {
            override def apply(x: A) {
                ch ! Msg(x)
            }
            override def exit(q: Exit) {
                ch ! Msg(q)
            }
        }
    }
}
