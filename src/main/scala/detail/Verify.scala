

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Verify {
    def apply[A](assertion: Boolean): Unit = assert(assertion)
    def apply[A](assertion: Boolean, message: String): Unit = assert(assertion, message)
}
