

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Indices[A](_1: Seq[A]) extends Forwarder[Int] {
    override protected val delegate = _1.generate((0 until java.lang.Integer.MAX_VALUE).view)
}
