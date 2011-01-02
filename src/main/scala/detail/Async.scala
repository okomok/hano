

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Async() extends SeqProxy[Unit] {
    override val self = Seq.origin { body =>
        try {
            ThreadPool.executor.submit {
                new java.util.concurrent.Callable[Unit] {
                    override def call() = body
                }
            }
        } catch {
            case _: java.util.concurrent.RejectedExecutionException => {
                new Thread {
                    override def run() = body
                } start
            }
        }
    }
}
