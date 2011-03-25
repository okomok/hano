

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent


private[hano]
object PollWithin {
    def apply[A](q: concurrent.BlockingQueue[A], timeout: Long): A = {
        if (timeout < 0) {
            q.take()
        } else {
            val res = q.poll(timeout, concurrent.TimeUnit.MILLISECONDS)
            if (res == null) {
                throw new concurrent.TimeoutException
            } else {
                res
            }
        }
    }
}


private[hano]
object Polleach {
    def apply[A](q: java.util.Queue[A])(f: A => Unit) {
        var x = q.poll
        while (x != null) {
            f(x)
            x = q.poll
        }
    }
}
