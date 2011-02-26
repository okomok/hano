

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait ReactionProxy[-A] extends Reaction[A] with scala.Proxy {
    def self: Reaction[A]
    override protected def rawEnter(p: Entrance) = self.enter(p)
    override protected def rawApply(x: A) = self.apply(x)
    override protected def rawExit(q: Exit) = self.exit(q)
}
