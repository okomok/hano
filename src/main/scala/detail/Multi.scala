

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Multi[A](_1: Seq[Reaction[A]]) extends Reaction[A] {
    override protected def rawApply(x: A) = for (f <- _1) f(x)
    override protected def rawExit(q: Exit) = for (f <- _1) f.exit(q)
}
