

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Call-site process
 */
object Self extends Process {
    override def close() = ()

    override def `do`(f: Reaction[Unit]) {
        f.enter {
            Exit.Empty
        } applying {
            f()
        } exit {
            Exit.Success
        }
    }
}
