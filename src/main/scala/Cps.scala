

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations


private class FromCps[A](from: => A @continuations.suspendable) extends Seq[A] {
    override def forloop(f: A => Unit, k: Exit => Unit) {
        continuations.reset {
            f(from)
        }
    }
}
