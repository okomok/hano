

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations.{cpsParam, reset, shift}


/**
 * Contains utilities for cps.
 */
object cps {
    @annotation.equivalentTo("scala.util.continuations.reset")
    def apply[A](ctx: => A @cpsParam[A, Any]) = reset(ctx)

    def require(cond: Boolean): Unit @cpsParam[Any, Unit] =  (if (cond) Single(()) else Empty).toCps

    private[hano]
    def discardValue[A](v: => A @cpsParam[Unit, Unit]): A @cpsParam[Any, Unit] = {
        shift { k: (A => Any) =>
            reset {
                k(v)
                ()
            }
        }
    }
}
