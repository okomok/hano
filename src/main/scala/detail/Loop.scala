

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop(_1: Seq[_]) extends Resource[Unit] {
    @volatile private[this] var isActive = true
    override def context = _1.context
    override protected def closeResource() = isActive = false
    override protected def openResource(f: Reaction[Unit]) {
        f.tryRethrow(context) {
            while (isActive) {
                context.eval {
                    f()
                }
            }
        }
        f.exit(Exit.Closed)
    }
}
