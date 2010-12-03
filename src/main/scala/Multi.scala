

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Multi[A](_1: Seq[A => Unit]) extends (A => Unit) {
    override def apply(x: A) = for (f <- _1) f(x)
}