

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * The empty sequence in Self process
 */
object Empty extends Seq[Nothing] {
    override def process = Self
    override def forloop(f: Reaction[Nothing]) {
        f.enter {
            Exit.Empty
        } exit {
            Exit.Success
        }
    }
}
