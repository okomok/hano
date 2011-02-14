

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations.{cpsParam, reset, shift}


/**
 * Contains utilities for cps.
 */
object cps {

    def require(cond: Boolean): Unit @cpsParam[Any, Unit] =  (if (cond) Single(()) else Empty).toCps

}
