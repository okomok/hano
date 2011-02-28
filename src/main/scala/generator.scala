

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations.{cpsParam, suspendable}


object generator {
    /**
     * Yielding thread is blocked.
     */
    object sync {
        type Env[A] = Reaction[A] with java.io.Flushable
        def apply[A](body: Env[A] => Unit): Iterable[A] = new detail.SyncIterable(new BodyToSeq(body))
    }

    /**
     * Yielding thread is not blocked; elements are buffered.
     */
    object async {
        type Env[A] = Reaction[A]
        def apply[A](body: Env[A] => Unit): Iterable[A] = new detail.AsyncIterable(new BodyToSeq(body))
    }

    /**
     * Single-threaded.
     */
    object cps {
        abstract class Env[A] {
            def apply(x: A): Unit @suspendable
            def amb[B](xs: Iter[B]): B @cpsParam[Any, Unit]
            def get[B](xs: Seq[B]): B @cpsParam[Any, Unit] = {
                require(xs.context eq Self)
                new detail.CheckSingle(xs).toCps
            }
            def require(cond: Boolean): Unit @cpsParam[Any, Unit] =  (if (cond) Single(()) else Empty).toCps
        }
        def apply[A](body: Env[A] => Any @suspendable): Iterable[A] = new detail.CpsIterable(body)
    }

    private class BodyToSeq[A](_1: Reaction[A] with java.io.Flushable => Unit) extends Seq[A] {
        override def context = Self
        override def forloop(f: Reaction[A]) {
            val g = new ToFlushable(f)
            try {
                g.enter()
                _1(g)
                g.exit(Exit.Success)
            } catch {
                case t: Throwable => g.exit(Exit.Failure(t))
            }
        }
    }

    private class ToFlushable[A](_1: Reaction[A]) extends ReactionProxy[A] with java.io.Flushable {
        override val self = _1
        override def flush() = _1 match {
            case f: java.io.Flushable => f.flush()
            case _ => ()
        }
    }
}
