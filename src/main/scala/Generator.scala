

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.ArrayDeque
import java.util.concurrent


object Generator {

    /**
     * Creates an Iterable from a body using Env.
     */
    def apply[A](body: Env[A] => Unit): Iterable[A] = Iter.from(new CursorImpl(body)).able

    /**
     * Converts a Traversable to Iterable using a thread.
     */
    def traverse[A](xs: scala.collection.TraversableOnce[A]): Iterable[A] = apply { * =>
        *.tryCatch {
            xs.foreach(*(_))
        }
        *.end()
    }

    /**
     * Provides method set used in a body.
     */
    sealed abstract class Env[-A] extends Reaction[A] {
        def flush(): Unit
    }

    private class CursorImpl[A](body: Env[A] => Unit) extends Cursor[A] {
        private[this] var in = new Data[A]
        private[this] val xch = new concurrent.Exchanger[Data[A]]

        eval.Threaded { new Task(body, xch).run() }
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

        private def doExchange() {
            assert(in.buf.isEmpty)
            in = xch.exchange(in)
            assert(!in.buf.isEmpty || in.isLast)
        }

        private def forwardExn() {
            if (in.buf.isEmpty && in.isLast && !in.exn.isEmpty) {
                throw in.exn.get
            }
        }
    }

    private val CAPACITY = 20

    private class Task[A](body: Env[A] => Unit, xch: concurrent.Exchanger[Data[A]]) extends Runnable {
        private[this] var out = new Data[A]

        private[this] val y = new Env[A] with CheckedReaction[A] {
            override protected def checkedApply(x: A) {
                out.buf.addLast(x)
                if (out.buf.size == CAPACITY) {
                    doExchange()
                }
            }
            private def _exit() {
                out.isLast = true
                doExchange()
            }
            override protected def checkedExit(q: Exit) {
                q match {
                    case Exit.Failed(why) => {
                        out.exn = Some(why)
                        _exit()
                    }
                    case _ => _exit()
                }
            }
            override def flush() {
                if (!out.buf.isEmpty) {
                    doExchange()
                }
            }
        }

        override def run() {
            try {
                body(y)
            } catch {
                case t: Throwable => y.failed(t)
            }
/*
            try {
                body(y)
            } catch {
                case t: Throwable => out.exn = Some(t)
            } finally {
                out.isLast = true
                doExchange()
            }
*/
        }

        private def doExchange() {
            out = xch.exchange(out)
            assert(out.buf.isEmpty)
        }
    }

    private class Data[A](val buf: ArrayDeque[A], var isLast: Boolean, var exn: Option[Throwable]) {
        def this() = this(new ArrayDeque[A](CAPACITY), false, None)
    }

}

