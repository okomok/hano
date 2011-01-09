

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Contains synchronous algorithms.
 */
object Sync {


    @Annotation.visibleForTesting
    final class Val[A] extends CheckedReaction[A] { self =>
        private[this] var v: Either[Throwable, A] = null
        private[this] val c = new java.util.concurrent.CountDownLatch(1)

        override protected def checkedApply(x: A) {
            if (v == null) {
                try {
                    v = Right(x)
                } finally {
                    c.countDown()
                }
            }
        }
        override protected def checkedExit(q: Exit) {
            try {
                q match {
                    case Exit.Failed(t) if v == null => v = Left(t)
                    case _ => ()
                }
            } finally {
                c.countDown()
            }
        }

        def apply(): A = {
            c.await()
            if (v == null) {
                throw new NoSuchElementException("Sync.Val.apply()")
            }
            v match {
                case Left(t) => throw t
                case Right(r) => r
            }
        }

        def toFunction: () => A = () => self.apply()
    }


    def isEmpty(xs: Seq[_]): () => Boolean = {
        val v = new Val[Boolean]
        xs `for` { x =>
            v(false)
            xs.close()
        } exit {
            case Exit.End => v(true)
            case q => v.exit(q)
        }
        v.toFunction
    }


    def length(xs: Seq[_]): () => Int = {
        val v = new Val[Int]
        var acc = 0
        xs `for` { x =>
            acc += 1
        } exit {
            case Exit.End => v(acc)
            case q => v.exit(q)
        }
        v.toFunction
    }

    def size(xs: Seq[_]): () => Int = length(xs)


    def head[A](xs: Seq[A]): () => A = {
        val v = new Val[A]
        var go = true
        xs `for` { x =>
            if (go) {
                go = false
                v(x)
                xs.close()
            }
        } exit {
            v.exit(_)
        }
        v.toFunction
    }

    def last[A](xs: Seq[A]): () => A = {
        val v = new Val[A]
        var acc: Option[A] = None
        xs `for` { x =>
            acc = Some(x)
        } exit {
            case Exit.End => if (acc.isEmpty) v.exit(Exit.End) else v(acc.get)
            case q => v.exit(q)
        }
        v.toFunction
    }

    def nth[A](xs: Seq[A])(n: Int): () => A = head(xs.drop(n))


    def foldLeft[A, B](xs: Seq[A])(z: B)(op: (B, A) => B): () => B = {
        val v = new Val[B]
        var acc = z
        xs `for` { x =>
            acc = op(acc, x)
        } exit {
            case Exit.End => v(acc)
            case q => v.exit(q)
        }
        v.toFunction
    }

    def reduceLeft[A](xs: Seq[A])(op: (A, A) => A): () => A = {
        val v = new Val[A]
        var acc: Option[A] = None
        xs `for` { x =>
            if (acc.isEmpty) {
                acc = Some(x)
            } else {
                acc = Some(op(acc.get, x))
            }
        } exit {
            case Exit.End => if (acc.isEmpty) v.exit(Exit.End) else v(acc.get)
            case q => v.exit(q)
        }
        v.toFunction
    }


    def min[A](xs: Seq[A])(implicit c: Ordering[A]): () => A = reduceLeft(xs)(c.min(_, _))

    def max[A](xs: Seq[A])(implicit c: Ordering[A]): () => A = reduceLeft(xs)(c.max(_, _))


    /**
     * Copies elements to a standard collection.
     */
    def copy[A, To](xs: Seq[A])(implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): () => To = {
        val v = new Val[To]
        var b = bf()
        xs `for` {
            b += _
        } exit {
            case Exit.End => v(b.result)
            case q => v.exit(q)

        }
        v.toFunction
    }


    /**
     * Evaluates `body` in a context.
     */
    def eval[A](ctx: Context)(body: => A): () => A = head(ctx.map(_ => body))

    /**
     * Evaluates `body` in the event-dispatch-thread.
     */
    def inEdt[A](body: => A): () => A = eval(Context.inEdt)(body)

    /*
     * Evaluates `body` in the thread-pool, or result-retrieving-site.
    def future[A](body: => A): () => A = {
        try {
            eval(Context.parallel)(body)
        } catch {
            case _: java.util.concurrent.RejectedExecutionException => () => body
        }
    }
     */
}
