

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Slice[A](_1: Seq[A], _2: Int, _3: Int) extends Forwarder[A] {
    Pre.range(_2, _3, "slice")
    override protected val delegate = _1.drop(_2).take(_3 - _2)
}
