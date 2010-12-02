

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


import scala.actors.Actor


/**
 * Runs in an actor.
 */
case class React[R](_1: ByName[R], _2: Actor) extends Function0[R] {
    private[this] val f = new Invoke(_1, r => _2 ! Reaction(ByName{r.run}))
    override def apply = f()
}

object React {
    def in(a: Actor): Strategy = new Strategy {
        override def apply[R](f: ByName[R]) = new React(f, a)
        override def apply[R](f: => R) = new React(f, a)
    }

}

case class Reaction(_1: Function0[Unit])
