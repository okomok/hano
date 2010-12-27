

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ByName[A](_1: eval.ByName[Seq[A]]) extends SeqProxy[A] {
    override def self = _1()
}
