

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ZipWithPick[A, B](_1: Seq[A], _2: Seq[B]) extends SeqAdapter.Of[(A, Option[B])](_1) {
    override def forloop(f: Reaction[(A, Option[B])]) {
        @volatile var _y: Option[B] = None
        @volatile var _p: Exit = null

        _2.onEnter { p =>
            _p = p
        } onEach { y =>
            _y = Some(y)
        } start()

        _1.onEnter {
            f.enter(_)
        } onEach { x =>
            f((x, _y))
        } onExit { q =>
            f.exit(q)
            _p(q)
        } start()
    }
}



private[hano]
class Pick[A](_1: Seq[A]) extends Iterable[Option[A]] {
    override def iterator = {
        import Pick._
        val data = new Data[A]
        _1.forloop(new ReactionImpl(data))
        new IteratorImpl(data).concrete
    }
}


private[hano]
object Pick {
    private class Data[A] {
        @volatile var value: Option[A] = None
        @volatile var isEnd = false
        @volatile var exitable = false
    }

    private class ReactionImpl[A](_data: Data[A]) extends Reaction[A] {
        override protected def rawEnter(p: Exit) {
            if (_data.exitable) {
                exit()
            }
        }
        override protected def rawApply(x: A) {
            _data.value = Some(x)
            if (_data.exitable) {
                exit()
            }
        }
        override protected def rawExit(q: Exit.Status) {
            _data.isEnd = true
        }
    }

    private class IteratorImpl[A](_data: Data[A]) extends AbstractIterator[Option[A]] {
        override def isEnd = _data.isEnd
        override def deref = _data.value
        override def increment() = ()
        override def close() = _data.exitable = true
    }
}
