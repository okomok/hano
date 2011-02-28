

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Always fails without elements.
 */
case class Fail(why: Throwable) extends Seq[Nothing] {
    override def context = Self

    override def forloop(f: Reaction[Nothing]) {
        f.enter()
        f.exit(Exit.Failure(why))
    }
}
