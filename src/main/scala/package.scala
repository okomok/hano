

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok


import scala.util.continuations


package object hano {

    /**
     * Calls all the functions in a Seq sequence.
     */
    def multi[A](fs: Seq[A => Unit]): A => Unit = new Multi(fs)

    @Annotation.equivalentTo("continuations.reset(ctx(BlockEnv))")
    def block[A](ctx: BlockEnv.type => A @continuations.cpsParam[A, Any]): Unit = continuations.reset(ctx(BlockEnv))

}
