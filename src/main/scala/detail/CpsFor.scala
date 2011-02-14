

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


import scala.util.continuations.{cpsParam, reset}


private[hano]
class CpsFor[+A](_1: Seq[A]) {
    def foreach(g: A => Any @cpsParam[Unit, Unit]): Exit @cpsParam[Any, Unit] = new Seq[Exit] {
        override def context = _1.context
        override def forloop(cp: Reaction[Exit]) {
            _1.onExit(q => cp(q)).forloop(Reaction(x => reset{g(x);()}, q => cp.exit(q)))
        }
    } toCps
}
