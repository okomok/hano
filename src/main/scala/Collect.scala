

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Collect[A, B](_1: Seq[A], _2: PartialFunction[A, B]) extends Forwarder[B] {
    override protected val delegate = _1.filter(_2.isDefinedAt).map(_2)
}
