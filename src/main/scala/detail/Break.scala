

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Break[A](_1: Seq[A]) extends Seq[A] {
    override def close() = _1.close()
    override def forloop(f: A => Unit, k: Exit => Unit) = _1.start()
    override def break: Seq[A] = _1.break // break.break fusion
}
