

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Trivial wrapper for a function passed around Actors.
 */
case class Action(_1: () => Unit) extends Message


object Action {
    def apply(body: => Unit)(implicit d: DummyImplicit) = new Action(() => body)
}
