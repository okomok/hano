

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.ArrayDeque
import java.util.concurrent.Exchanger


object Generator {


    /**
     * Creates an Iterable from a body using Env.
     */
    def apply[A](body: Env[A] => Unit): Iterable[A] = new Apply(body)

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
    sealed abstract class Env[-A] extends Reaction[A] {
        /**
         * Forces buffered data to be passed.
         */
        def flush(): Unit
    }


    private def threaded(body: => Unit) {
        new Thread {
            override def run() {
                body
            }
        } start()
    }


    private class Apply[A](_1: Env[A] => Unit) extends Iterable[A] {
        override def iterator = {
            val xch = new Exchanger[Data[A]]
            val f = new ReactionImpl(xch)
            threaded {
                try {
                    _1(f)
                } catch {
                    case t: Throwable if !f.exited => f.exit(Exit.Failed(t))
                }
            }
            Iter.from(new CursorImpl(xch)).begin
        }
    }


    private[hano] class SeqToIterable[A](_1: Seq[A]) extends Iterable[A] {
        override def iterator = {
            val xch = new Exchanger[Data[A]]
            if (_1.context eq Context.self) {
                threaded {
                    _1.forloop(new ReactionImpl(xch))
                }
            } else {
                _1.forloop(new ReactionImpl(xch))
            }
            Iter.from(new CursorImpl(xch)).begin
        }
    }


    private val CAPACITY = 20

    private class Data[A](val buf: ArrayDeque[A], var isLast: Boolean, var exn: Option[Throwable]) {
        def this() = this(new ArrayDeque[A](CAPACITY), false, None)
    }


    private class CursorImpl[A](xch: Exchanger[Data[A]]) extends Cursor[A] {

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


    private class ReactionImpl[A](xch: Exchanger[Data[A]]) extends Env[A] with CheckedReaction[A] {

        private[this] var out = new Data[A]
        private[hano] var exited = false

        override protected def checkedApply(x: A) {
            out.buf.addLast(x)
            if (out.buf.size == CAPACITY) {
                doExchange()
            }
        }
        override protected def checkedExit(q: Exit) {
            exited = true
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
