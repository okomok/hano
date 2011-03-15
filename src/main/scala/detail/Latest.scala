

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail

/*
private[hano]
class Latest[A, B >: A](_1: Seq[A], _2: B) extends Iterable[B] {
    override def iterator = {
        import Latest._
        val data = new Data(_2)
        _1.forloop(new ReactionImpl(data))
        new IteratorImpl(data).concrete
    }
}


private[hano]
object Latest {
    private class Data[A](z: A) {
        val ch = new Channel[A]
        @volatile var value = new Val[A]
        @volatile var isEnd = false
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
            _data.foreach { _ =>
                ()
            }
            _data.ch << x
        }
        override protected def rawExit(q: Exit.Status) {
            _data.isEnd = true
        }
    }

    private class IteratorImpl[A](_data: Data[A], t: Within) extends AbstractIterator[A] {
        override def isEnd = _data.isEnd
        override def deref = _data.value(t)
        override def increment() = ()
        override def close() {
            _data.exitable = true
            _data.exit()
        }
    }
}
*/
