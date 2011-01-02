

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Threaded() extends SeqProxy[Unit] {
    override val self = Seq.origin { body =>
        new Thread {
            override def run() = body
        } start
    }
}
