

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object AsyncForloop {
    def apply[A](xs: Seq[A])(f: Reaction[A]) {
        if (xs.context eq Context.self) {
            new Thread {
                override def run() {
                    xs.forloop(f)
                }
            } start()
        } else {
            xs.forloop(f)
        }
    }
}
