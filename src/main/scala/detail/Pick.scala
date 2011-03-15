

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
        new IteratorImpl(data)
    }
}


private[hano]
object Pick {

    private class Data[A](z: A) {
        @volatile var mail = ElementMail(z).asMail
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
            _data.mail = ElementMail(x)
        }
        override protected def rawExit(q: Exit.Status) {
            _data.mail = ExitMail(q)
        }
    }

    private class IteratorImpl[A](_data: Data[A]) extends Iterator[A] with java.io.Closeable {
        override def hasNext = _data.mail match {
            case ExitMail(Exit.Failure(t)) => throw t
            case ExitMail(_) => false
            case ElementMail(_) => true
        }
        override def next = _data.mail.element

        override def close() {
            _data.exitable = true
            _data.exit()
        }
    }
}
