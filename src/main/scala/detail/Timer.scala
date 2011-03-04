

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object _Timer {
    val daemon = new Timer(true)
    val nondaemon = new Timer(false)
}
