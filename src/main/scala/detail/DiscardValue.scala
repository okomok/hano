

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class DiscardValue[T, U](_1: T => U) extends (T => Unit) {
    override def apply(x: T) = _1(x)
}
