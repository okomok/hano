

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Trivial wrapper for a function passed around Actors.
 */
case class Action(_1: () => Unit)


object Action {
    def apply[U](body: => U)(implicit d: DummyImplicit) = new Action(() => body)
}
