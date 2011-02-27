

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Flatten[A](_1: Seq[Seq[A]]) extends SeqProxy[A] {
    override val self = _1.flatMap(x => x)
}
