

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok


package object hano {

    @annotation.returnThat
    def from[A](that: Seq[A]): Seq[A] = that

    @annotation.returnThat
    def use[A](that: Arm[A]): Arm[A] = that

    /**
     * Breaks the current reaction.
     */
    def break(): Nothing = throw BreakControl

    /**
     * Continues the current reaction.
     */
    def continue(): Nothing = throw detail.ContinueControl

    /**
     * Creates an asynchronous process in the thread-pool.
     */
    @annotation.aliasOf("new Async()")
    def async: Process = new Async()

    /**
     * By-name sequence
     */
    @annotation.processShifted
    def byName[A](body: => Seq[A]): Seq[A] = new detail.ByName(() => body)

    /**
     * A reaction as set of reactions
     */
    def multi[A](xs: Seq[Reaction[A]]): Reaction[A] = new detail.Multi(xs)

    /**
     * Builds single-or-empty sequence from an expression.
     */
    def optional[A](body: => A): Seq[A] = new detail.Optional(() => body)

    private[hano]
    val NO_TIMEOUT: Long = -1
}
