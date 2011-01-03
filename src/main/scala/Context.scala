

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * A context is an infinite sequence of Units.
 */
object Context {


    /**
     * An infinite sequence of Units
     */
    def origin(k: (=> Unit) => Unit): Seq[Unit] = new Origin(k)

    /**
     * An infinite sequence of Units in the call-site
     */
    def strict: Seq[Unit] = new Strict()

    /**
     * An infinite sequence of Units in a new thread
     */
    def threaded: Seq[Unit] = new Threaded()

    /**
     * An infinite sequence of Units in a thread-pool
     */
    def parallel: Seq[Unit] = new Parallel()

    /**
     * An infinite sequence of Units in a thread-pool or new thread
     */
    def async: Seq[Unit] = new Async()

    /**
     * An infinite sequence of Units in the event-dispatch-thread
     */
    def inEdt: Seq[Unit] = new InEdt()

    /**
     * Evaluates `body` in a context.
     *   cf. http://lampsvn.epfl.ch/trac/scala/ticket/302
     */
    def eval(ctx: => Seq[Unit]): (=> Unit) => Unit = new Eval(ctx)


    private class Origin(_1: (=> Unit) => Unit) extends Seq[Unit] {
        @volatile private[this] var isActive = false
        override def close() = isActive = false
        override def forloop(f: Reaction[Unit]) = synchronized {
            isActive = true
            _1 {
                f.tryCatch {
                    while (isActive) {
                        f()
                    }
                }
                f.exit(Exit.Closed)
            }
        }
    }

    private class Strict() extends SeqProxy[Unit] {
        override val self = origin { body =>
            body
        }
    }

    private class Threaded() extends SeqProxy[Unit] {
        override val self = origin { body =>
            new Thread {
                override def run() = body
            } start
        }
    }

    private class Parallel() extends SeqProxy[Unit] {
        override val self = origin { body =>
            detail.ThreadPool.submit(body)
        }
    }

    private class Async() extends SeqProxy[Unit] {
        override val self = origin { body =>
            try {
                detail.ThreadPool.submit(body)
            } catch {
                case _: java.util.concurrent.RejectedExecutionException => {
                    new Thread {
                        override def run() = body
                    } start
                }
            }
        }
    }

    private class InEdt() extends SeqProxy[Unit] {
        override val self = origin { body =>
            javax.swing.SwingUtilities.invokeLater {
                new Runnable {
                    override def run() = body
                }
            }
        }
    }

    private class Eval(_1: => Seq[Unit]) extends ((=> Unit) => Unit) {
        override def apply(body: => Unit) = _1.take(1).foreach(_ => body)
    }

}
