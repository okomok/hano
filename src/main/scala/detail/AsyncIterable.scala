

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class AsyncIterable[A](_1: Seq[A]) extends Iterable[A] {
    override def iterator = {
        import AsyncIterable._
        val ch = new Channel[Msg]
        AsyncForloop(_1)(new ReactionImpl[A](ch))
        new IteratorImpl[A](ch).concrete
    }
}


private[hano]
object AsyncIterable {
    private case class Msg(msg: Any)

    private class ReactionImpl[A](ch: Channel[Msg]) extends Reaction[A] {
        override protected def rawEnter(p: Exit) = ()
        override protected def rawApply(x: A) = ch write Msg(x)
        override protected def rawExit(q: Exit.Status) = ch write Msg(q)
    }

    private class IteratorImpl[A](ch: Channel[Msg]) extends AbstractIterator[A] {
        private[this] var v: Option[A] = None
        ready()

        override def isEnd = v.isEmpty
        override def deref = v.get
        override def increment() = ready()

        private def ready() {
            v = ch.read match {
                case Msg(Exit.Failure(t)) => throw t
                case Msg(q: Exit.Status) => None
                case Msg(x) => Some(x.asInstanceOf[A])
            }
        }
    }
}
