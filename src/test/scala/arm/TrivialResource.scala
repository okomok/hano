

// Copyright Shunsuke Sogame 2008-2009.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package armtest


import com.github.okomok.hano


case class TrivialResourceException(msg: String) extends RuntimeException(msg)


case class TrivialResource[A](res: A, b: Boolean = false) extends hano.Arm[A] {
    var began = false
    var ended = false

    override def open = {
        if (b) throw new TrivialResourceException("from TrivialResource")
        began = true
        res
    }
    override def close {
        ended = true
    }
}


class TrivialCloseable extends java.io.Closeable {
    var ended = false
    override def close {
        ended = true
    }
}
