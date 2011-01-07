

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Action {
    def apply(body: => Unit)(implicit d: DummyImplicit) = new Action(() => body)
}

private[hano]
case class Action(_1: () => Unit)
