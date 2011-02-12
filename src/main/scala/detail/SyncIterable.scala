

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.ArrayDeque
import java.util.concurrent.Exchanger


private[hano]
class SyncIterable[A](_1: Seq[A]) extends Iterable[A] {
    override def iterator = {
        import SyncIterable._
        val xch = new Exchanger[Data[A]]
        AsyncForloop(_1)(new ReactionImpl(xch))
        new IteratorImpl(xch).concrete
    }
}


private[hano]
object SyncIterable {

    private val CAPACITY = 20

    private class Data[A](val buf: ArrayDeque[A], var isLast: Boolean, var exn: Option[Throwable]) {
        def this() = this(new ArrayDeque[A](CAPACITY), false, None)
    }

    private class IteratorImpl[A](xch: Exchanger[Data[A]]) extends AbstractIterator[A] {
        private[this] var in = new Data[A]

        doExchange()
        forwardExn()

        override def isEnd = {
            in.buf.isEmpty
        }
        override def deref = {
            in.buf.getFirst
        }
        override def increment() {
            in.buf.removeFirst()
            if (in.buf.isEmpty && !in.isLast) {
                doExchange()
            }
            forwardExn()
        }

        private def forwardExn() {
            if (in.buf.isEmpty && in.isLast && !in.exn.isEmpty) {
                throw in.exn.get
            }
        }

        private def doExchange() {
            assert(in.buf.isEmpty)
            in = xch.exchange(in)
            assert(!in.buf.isEmpty || in.isLast)
        }
    }

    private class ReactionImpl[A](xch: Exchanger[Data[A]]) extends Reaction[A] with java.io.Flushable {
        private[this] var out = new Data[A]

        override protected def rawApply(x: A) {
            out.buf.addLast(x)
            if (out.buf.size == CAPACITY) {
                doExchange()
            }
        }
        override protected def rawExit(q: Exit) {
            q match {
                case Exit.Failed(t) => {
                    out.exn = Some(t)
                }
                case _ => ()
            }
            out.isLast = true
            doExchange()
        }

        override def flush() {
            if (!out.buf.isEmpty) {
                doExchange()
            }
        }

        private def doExchange() {
            out = xch.exchange(out)
            assert(out.buf.isEmpty)
        }
    }
}
