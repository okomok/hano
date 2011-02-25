

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Trivial helper to implement combinators.
 */
trait SeqAdapter[+A] extends Seq[A] {
    protected def underlying: Seq[_]
    override def context = underlying.context
}
