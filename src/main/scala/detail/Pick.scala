

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Pick[A, B >: A](_1: Seq[A], _2: B) extends Iterable[B] {
    override def iterator = {
        import Pick._
        val data = new Data(_2)
        _1.forloop(new ReactionImpl(data))
        new IteratorImpl(data).concrete
    }
}


private[hano]
object Pick {
    private class Data[A](z: A) {
        @volatile var value: A = z
        @volatile var isEnd = false
        @volatile var exit = Exit.Empty.asExit
        @volatile var exitable = false
    }

    private class ReactionImpl[A](_data: Data[A]) extends Reaction[A] {
        override protected def rawEnter(p: Exit) {
            _data.exit = p
            if (_data.exitable) {
                exit()
            }
        }
        override protected def rawApply(x: A) {
            _data.value = x
            if (_data.exitable) {
                exit()
            }
        }
        override protected def rawExit(q: Exit.Status) {
            _data.isEnd = true
        }
    }

    private class IteratorImpl[A](_data: Data[A]) extends AbstractIterator[A] {
        override def isEnd = _data.isEnd
        override def deref = _data.value
        override def increment() = ()
        override def close() {
            _data.exitable = true
            _data.exit()
        }
    }
}
