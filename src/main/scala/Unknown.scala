

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Unknown context
 */
object Unknown extends Context {
    override def close() = ()

    override def forloop(f: Reaction[Unit]) {
        throw new UnsupportedOperationException("Unknown.forloop")
    }
}
