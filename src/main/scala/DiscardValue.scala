

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class DiscardValue[T, U](_1: T => U) extends Function1[T, Unit] {
    override def apply(x: T) = _1(x)
}