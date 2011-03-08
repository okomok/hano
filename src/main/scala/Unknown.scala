

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Unknown process
 */
object Unknown extends Process {
    override def close() = ()

    override def `do`(f: Reaction[Unit]) {
        throw new UnsupportedOperationException("Unknown.do")
    }
}
