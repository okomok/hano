

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Asynchronous[A](_1: Seq[A]) extends Seq[A] {
    override val process = async

    override def forloop(f: Reaction[A]) {
        _1.shift {
            process
        } shiftStart {
            process
        } forloop(f)
    }
}
