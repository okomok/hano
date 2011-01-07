

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Body {
    def apply(body: => Unit)(implicit d: DummyImplicit) = new Body(() => body)
}

private[hano]
case class Body(_1: () => Unit)
