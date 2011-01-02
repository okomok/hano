

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Origin(_1: (=> Unit) => Unit) extends Seq[Unit] {
    @volatile private[this] var isActive = false
    override def close() = isActive = false
    override def forloop(f: Reaction[Unit]) = synchronized {
        isActive = true
        _1 {
            f.tryCatch {
                while (isActive) {
                    f()
                }
            }
            f.exit(Exit.Closed)
        }
    }
}
