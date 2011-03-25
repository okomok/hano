

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Actor message
 */
sealed abstract class Message


/**
 * `Function0` wrapper
 */
case class Action(_1: () => Unit) extends Message

object Action {
    // `U` disambiguates overload resolution.
    def apply[U](body: => U)(implicit d: DummyImplicit) = new Action(() => body)
}


/**
 * For an actor to exit
 */
case object Close extends Message
