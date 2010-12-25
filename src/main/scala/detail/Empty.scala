

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Empty() extends Seq[Nothing] {
    override def forloop(f: Reaction[Nothing]) = f.onExit(Exit.End)
}