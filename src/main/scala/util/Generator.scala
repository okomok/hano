

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import java.util.ArrayDeque
import java.util.concurrent


object Generator {

    def apply[A](body: Env[A] => Unit) = new Iterable[A] {
        override def iterator = new CursorImpl(body).toIterator
    }

    sealed abstract class Env[-A] extends (A => Unit) {
        def exit(): Unit
        def flush(): Unit
    }

    private class CursorImpl[A](body: Env[A] => Unit) extends Cursor[A] {
        private[this] var in = new Data[A]
        private[this] val ex = new concurrent.Exchanger[Data[A]]

        hano.eval.Async { new Task(body, ex).run() }
        doExchange()

        override def isEnd = in.buf.isEmpty
        override def deref = in.buf.getFirst
        override def increment() {
            in.buf.removeFirst()
            if (in.buf.isEmpty && !in.isLast) {
                doExchange()
            }
        }

        private def doExchange() {
            assert(in.buf.isEmpty)
            in = ex.exchange(in)
            assert(!in.buf.isEmpty || in.isLast)
        }
    }

    private val CAPACITY = 20

    private class Task[A](body: Env[A] => Unit, ex: concurrent.Exchanger[Data[A]]) extends Runnable {
        private[this] var out = new Data[A]

        private[this] val y = new Env[A] {
            override def apply(x: A) {
                out.buf.addLast(x)
                if (out.buf.size == CAPACITY) {
                    doExchange()
                }
            }
            override def exit() {
                out.isLast = true
                doExchange()
            }
            override def flush() {
                if (!out.buf.isEmpty) {
                    doExchange()
                }
            }
        }

        override def run() {
            body(y) // Exceptions will disappear in eval.Async.
        }

        private def doExchange() {
            out = ex.exchange(out)
            assert(out.buf.isEmpty)
        }
    }

    private class Data[A](val buf: ArrayDeque[A], var isLast: Boolean) {
        def this() = this(new ArrayDeque[A](CAPACITY), false)
    }

}
