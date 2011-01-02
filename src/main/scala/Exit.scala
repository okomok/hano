

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


sealed abstract class Exit


object Exit {

    case object End extends Exit

    case object Closed extends Exit

    case class Failed(why: Throwable) extends Exit

}
