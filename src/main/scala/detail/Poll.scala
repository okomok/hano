

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import java.util.concurrent


/**
 * Retrieves and removes the head of this queue,
 * waiting if no elements are present on this queue.
 */
private[hano]
object Poll {
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
