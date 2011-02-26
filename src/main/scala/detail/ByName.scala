

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ByName[A](_1: () => Seq[A]) extends Seq[A] {
    override def context = Unknown

    override def forloop(f: Reaction[A]) {
        _1().onEnter {
            f.enter(_)
        } onEach {
            f(_)
        } onExit {
            f.exit(_)
        } start()
    }
}
