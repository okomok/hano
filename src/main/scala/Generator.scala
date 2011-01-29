

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations.{cpsParam, suspendable}


object Generator {

    /**
     * Yielding thread is blocked.
     */
    object Sync {
        type Env[A] = Reaction[A] with java.io.Flushable
        def apply[A](body: Env[A] => Unit): Iterable[A] = new detail.SyncIterable(new BodyToSeq(body))
    }

    /**
     * Yielding thread is not blocked; elements are buffered.
     */
    object Async {
        type Env[A] = Reaction[A]
        def apply[A](body: Env[A] => Unit): Iterable[A] = new detail.AsyncIterable(new BodyToSeq(body))
    }

    /**
     * Single-threaded.
     */
    object Cps {
        abstract class Env[A] extends Block.Env1 {
            def apply(x: A): Unit @suspendable
            def amb[B](xs: Iter[B]): B @cpsParam[Any, Unit]
        }
        def apply[A](body: Env[A] => Any @suspendable): Iterable[A] = new detail.CpsIterable(body)
    }

    private class BodyToSeq[A](_1: Reaction[A] with java.io.Flushable => Unit) extends Seq[A] {
        override def context = Self
        override def forloop(f: Reaction[A]) {
            val g = new MultiExitable(f)
            try {
                _1(g)
                g.exit(Exit.End)
            } catch {
                case t: Throwable => g.exitNothrow(Exit.Failed(t))
            }
        }
    }

    // Accepts multiple exit calls, though it's illegal.
    private class MultiExitable[A](_1: Reaction[A]) extends Reaction[A] with java.io.Flushable {
        private[this] var exited = false
        override def flush() = _1 match {
            case f: java.io.Flushable => f.flush()
            case _ => ()
        }
        override def apply(x: A) = _1(x)
        override def exit(q: Exit) {
            if (!exited) {
                exited = true
                _1.exit(q)
            } else {
                q match {
                    case Exit.Failed(t) => detail.LogErr(t, "abandoned exception in Generator")
                    case _ => ()
                }
            }
        }
    }
}
