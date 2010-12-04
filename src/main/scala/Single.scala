

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Single[A](_1: A) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        Exit.tryCatch(k) {
            f(_1)
        }
        k(Exit.End)
    }
}
