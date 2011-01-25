

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class Unknown() extends Context {
    override def forloop(f: Reaction[Unit]) {
        throw new Error
    }
}
