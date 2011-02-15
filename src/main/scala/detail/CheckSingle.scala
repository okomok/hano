

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class CheckSingle[A](_1: Seq[A]) extends SeqProxy[A] {
    override val self = {
        _1.onNth(1) {
            case Some(x) => throw new UnsupportedOperationException("Seq length must be 0 or 1") with SeriousException
            case None => ()
        }
    }
}