

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Optional[A](_1: () => A) extends SeqProxy[A] {
    override val self = Seq.from {
        try {
            Some(_1())
        } catch {
            case _ => None
        }
    }
}
