

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Latest[A](_1: Seq[A], _2: Within) extends Iterable[A] {
    override def iterator = {
        import Latest._
        val data = new Data[A]
        _1.forloop(new ReactionImpl(data))
        new IteratorImpl(data, _2).concrete
    }
}


private[hano]
object Latest {

    private class Data[A] {
        val queue = new java.util.concurrent.LinkedBlockingQueue[Mail[A]](1)
        @volatile var exit = Exit.Empty.asExit
        @volatile var exitable = false
    }

    private class ReactionImpl[A](_data: Data[A]) extends Reaction[A] {
        override protected def rawEnter(p: Exit) {
            if (_data.exitable) {
                exit()
            }
            _data.exit = p
        }

        override protected def rawApply(x: A) {
            if (_data.exitable) {
                exit()
            }
            _data.queue.clear()
            Verify(_data.queue.offer(ElementMail(x)))
        }

        override protected def rawExit(q: Exit.Status) {
            _data.queue.clear()
            Verify(_data.queue.offer(ExitMail(q)))
        }
    }

    private class IteratorImpl[A](_data: Data[A], _t: Within) extends AbstractIterator[A] {
        private[this] var _x: Option[A] = None
        _ready()

        override def isEnd = _x.isEmpty
        override def deref = _x.get
        override def increment() = _ready()

        override def close() {
            _data.exitable = true
            _data.exit()
        }

        private def _ready() {
            _x = _t.poll(_data.queue).toOption
        }
    }
}
