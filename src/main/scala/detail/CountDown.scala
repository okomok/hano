

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object CountDown {
    def apply[A](c: java.util.concurrent.CountDownLatch)(body: => A): A = {
        try {
            body
        } finally {
            c.countDown()
        }
    }
}
