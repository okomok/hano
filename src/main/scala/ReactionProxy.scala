

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait ReactionProxy[-A] extends Reaction[A] with scala.Proxy {
    def self: Reaction[A]
    override protected def rawApply(x: A): Unit = self.apply(x)
    override protected def rawExit(q: Exit): Unit = self.exit(q)
}
