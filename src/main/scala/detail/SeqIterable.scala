

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent.BlockingQueue


private[hano]
class SeqIterable[A](_1: Seq[A], _2: Long, _3: () => BlockingQueue[Any]) extends Iterable[A] {
    override def iterator = {
        import SeqIterable._
        val data = new Data(_3())
        async.invoke {
            _1.forloop(new ReactionImpl[A](data))
        }
        new IteratorImpl[A](data, _2).concrete
    }
}


private[hano]
object SeqIterable {

    private class Data(val queue: BlockingQueue[Any]) {
        @volatile var exit = Exit.Empty.asExit
        @volatile var exitable = false
    }

    private class ReactionImpl[A](_data: Data) extends Reaction[A] {
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
            _data.queue.put(ElementMail(x))
        }

        override protected def rawExit(q: Exit.Status) {
            _data.queue.put(ExitMail(q))
        }
    }

    private class IteratorImpl[A](_data: Data, _timeout: Long) extends AbstractIterator[A] {
        private[this] var _x: Option[A] = None
        _ready()

        override def isEnd = _x.isEmpty
        override def deref = _x.get
        override def increment = _ready()

        override def close() {
            _data.exitable = true
            _data.exit()
        }

        private def _ready() {
            _x = Poll(_data.queue, _timeout).asInstanceOf[Mail[A]].toOption
        }
    }
}
