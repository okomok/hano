

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Origin(_1: (=> Unit) => Unit) extends Seq[Unit] {
    @volatile private[this] var isActive = false
    override def close() = isActive = false
    override def forloop(f: Unit => Unit, k: Exit => Unit) = synchronized {
        assert(!isActive)
        isActive = true
        _1 {
            Exit.tryCatch(k) {
                while (isActive) {
                    f()
                }
            }
        }
    }
}
