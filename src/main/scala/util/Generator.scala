

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


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
        xs.foreach(*(_))
        *.end()
    }

    /**
     * Provides method set used in a body.
     */
    sealed abstract class Env[-A] extends hano.Reaction[A] {
        def flush(): Unit
    }

    private class CursorImpl[A](_1: Env[A] => Unit) extends Cursor[A] {
        private[this] var in = new Data[A]
        private[this] val x = new concurrent.Exchanger[Data[A]]

        hano.eval.Threaded { new Task(_1, x).run() }
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
            in = x.exchange(in)
            assert(!in.buf.isEmpty || in.isLast)
        }

        private def forwardExn() {
            if (in.buf.isEmpty && in.isLast && !in.exn.isEmpty) {
                throw in.exn.get
            }
        }
    }

    private val CAPACITY = 20

    private class Task[A](body: Env[A] => Unit, x: concurrent.Exchanger[Data[A]]) extends Runnable {
        private[this] var out = new Data[A]

        private[this] val y = new Env[A] with hano.Reaction.Checked[A] {
            override protected def applyChecked(e: A) {
                out.buf.addLast(e)
                if (out.buf.size == CAPACITY) {
                    doExchange()
                }
            }
            private[this] val _exit = hano.eval.Lazy {
                out.isLast = true
                doExchange()
            }
            override protected def exitChecked(q: hano.Exit) {
                q match {
                    case hano.Exit.End => _exit()
                    case hano.Exit.Closed => _exit()
                    case hano.Exit.Failed(why) => {
                        out.exn = Some(why)
                        _exit()
                    }
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
            out = x.exchange(out)
            assert(out.buf.isEmpty)
        }
    }

    private class Data[A](val buf: ArrayDeque[A], var isLast: Boolean, var exn: Option[Throwable]) {
        def this() = this(new ArrayDeque[A](CAPACITY), false, None)
    }

}

