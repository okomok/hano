

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.TimerTask


/**
 * A context is a sequence of Units.
 */
object Context {


    /**
     * Creates a context from `eval`.
     */
    def origin(eval: (=> Unit) => Unit): Seq[Unit] = new Origin(eval)

    /**
     * In the call-site
     */
    def strict: Seq[Unit] = new Strict()

    /**
     * In a new thread
     */
    def threaded: Seq[Unit] = new Threaded()

    /**
     * In the thread-pool
     */
    def parallel: Seq[Unit] = new Parallel()

    /**
     * In the thread-pool or a new thread
     */
    def async: Seq[Unit] = new Async()

    /**
     * In the event-dispatch-thread
     */
    def inEdt: Seq[Unit] = new InEdt()

    /**
     * In a timer
     */
    def inTimer(schedule: TimerTask => Unit): Seq[Unit] = new InTimer(schedule)

    /**
     * Evaluates `body` in a context.
     *   cf. http://lampsvn.epfl.ch/trac/scala/ticket/302
     */
    def eval(ctx: => Seq[Unit]): (=> Unit) => Unit = new Eval(ctx)


    private class Origin(_1: (=> Unit) => Unit) extends Resource[Unit] {
        @volatile private[this] var isActive = true
        override protected def closeResource() = isActive = false
        override protected def openResource(f: Reaction[Unit]) {
            _1 {
                f.tryRethrow {
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

    private class InTimer(_1: TimerTask => Unit) extends NoExitResource[Unit] {
        private[this] var l: TimerTask = null
        override protected def closeResource() = l.cancel()
        override protected def openResource(f: Unit => Unit) {
            l = new TimerTask {
                override def run() = f()
            }
            _1(l)
        }
    }

    private class Eval(_1: => Seq[Unit]) extends ((=> Unit) => Unit) {
        override def apply(body: => Unit) = _1.take(1).foreach(_ => body)
    }

}
