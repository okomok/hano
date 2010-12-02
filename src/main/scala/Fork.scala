

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Fork[A](_1: Seq[A], _2: Seq[A] => Seq[_]) extends Forwarder[A] {
    override protected lazy val delegate = {
        val (xs, ys) = _1.duplicate
        _2(xs).start()
        ys
    }
}
