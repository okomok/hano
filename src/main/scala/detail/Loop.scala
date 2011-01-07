

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Loop(_1: Context) extends Resource[Unit] {
    @volatile private[this] var isActive = true
    override def context = _1
    override protected def closeResource() = isActive = false
    override protected def openResource(f: Reaction[Unit]) {
        For(context) { _ =>
            while (isActive) {
                f()
            }
        } AndThen {
            case Exit.End => f.exit(Exit.Closed)
            case q => f.exit(q)
        }
    }
}
