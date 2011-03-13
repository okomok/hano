

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.concurrent.TimeUnit


sealed abstract class Within

object Within {
    case object Inf extends Within
    case class Elapse(duration: Long, unit: TimeUnit) extends Within

    def apply(msec: Long): Within = Elapse(msec, TimeUnit.MILLISECONDS)
    def apply(duration: Long, unit: TimeUnit): Within = Elapse(duration, unit)
}
