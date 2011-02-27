

// Copyright Shunsuke Sogame 2010-2011.
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
            _1.onEach { x =>
                reset {
                    g(x)
                    ()
                }
            } onExit { q =>
                cp.enter()
                cp(q)
                cp.exit(Exit.End)
            } start()
        }
    } toCps
}
