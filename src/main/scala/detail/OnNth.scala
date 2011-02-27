

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class OnNth[A](_1: Seq[A], _2: Int, _3: Option[A] => Unit) extends SeqProxy[A] {
    override val self = _1.fork{ r => r.drop(_2).onHead(_3).start() }
}
