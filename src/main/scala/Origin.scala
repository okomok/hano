

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Origin(_1: (=> Unit) => Unit) extends Resource[Unit] {
    @volatile private[this] var isClosed = false
    override protected def closeResource() = isClosed = true
    override protected def openResource(f: Unit => Unit, k: Exit => Unit) {
        _1 {
            Exit.tryCatch(k) {
                while (!isClosed) {
                    f()
                }
            }
        }
    }
}
