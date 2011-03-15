

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent.BlockingQueue


private[hano]
class ToIterable2[A](_1: Seq[A], _2: Within, _3: BlockingQueue[Any]) extends Iterable[A] {
    override def iterator = {
        import ToIterable2._

        val data = new Data(_3)

        if (_1.process eq Self) {
            new Thread {
                override def run() {
                    _1.forloop(new ReactionImpl[A](data))
                }
            } start()
        } else {
            _1.forloop(new ReactionImpl[A](data))
        }

        new IteratorImpl[A](data, _2).concrete
    }
}


private[hano]
object ToIterable2 {

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
            _data.queue.put(ElemMail(x))
        }

        override protected def rawExit(q: Exit.Status) {
            _data.queue.put(ExitMail(q))
        }
    }

    private class IteratorImpl[A](_data: Data, _t: Within) extends AbstractIterator[A] {
        private[this] var _x: Option[A] = None
        _ready()

        override def isEnd = !_x.isEmpty
        override def deref = _x.get
        override def increment = _ready()

        override def close() {
            _data.exitable = true
            _data.exit()
        }

        private def _ready() {
            _x = _t.poll(_data.queue).asInstanceOf[Mail[A]].toOption
        }
    }
}
