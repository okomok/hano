

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait ReactionProxy[-A] extends Reaction[A] with scala.Proxy {
    def self: Reaction[A]
    override def apply(x: A): Unit = self.apply(x)
    override def exit(q: Exit): Unit = self.exit(q)
}
