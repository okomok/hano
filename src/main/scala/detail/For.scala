

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class For[+A](xs: Seq[A], f: A => Unit) {

    def exit(k: Exit => Unit) {
        xs.forloop(Reaction(f, k))
    }

    def exit() {
        xs.foreach(f)
    }
}
