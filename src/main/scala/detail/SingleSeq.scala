

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


@annotation.optimization
private[hano]
trait SingleSeq[A] extends Seq[A] {
    override def head: Seq[A] = this
    override def last: Seq[A] = this
}
