

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Threaded {
    def apply(body: => Unit) {
        new Thread {
            override def run() {
                body
            }
        } start()
    }
}
