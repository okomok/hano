

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent._


private[hano]
object ThreadPool {

    def submit(body: => Unit) {
        executor.submit {
            new Callable[Unit] {
                override def call() = body
            }
        }
    }

    private val size: Int = 2 * java.lang.Runtime.getRuntime.availableProcessors

    // A task which has internal dependencies needs direct-handoffs(SynchronousQueue).
    // (scala.actors.Future doesn't support such tasks.)
    private val executor: ThreadPoolExecutor = {
        val ex = new ThreadPoolExecutor(0, size, 60L, TimeUnit.SECONDS, new SynchronousQueue[Runnable])
        ex.setThreadFactory(new DaemonThreadFactory(ex.getThreadFactory))
        ex
    }

    private class DaemonThreadFactory(underlying: ThreadFactory) extends ThreadFactory {
        override def newThread(r: Runnable) = {
            val t = underlying.newThread(r)
            t.setDaemon(true)
            t
        }
    }

}
