

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Flatten[A](_1: Seq[Seq[A]]) extends Forwarder[A] {
    override protected val delegate = _1.flatMap(x => x)
}
