

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations.{cpsParam, reset, shift}


object block {

    @annotation.equivalentTo("scala.util.continuations.reset")
    def apply[A](ctx: => A @cpsParam[A, Any]): Unit = reset(ctx)

    private[hano] def discardValue[A](v: => A @cpsParam[Unit, Unit]): A @cpsParam[Any, Unit] = {
        shift { k: (A => Any) =>
            reset {
                k(v)
                ()
            }
        }
    }
}
