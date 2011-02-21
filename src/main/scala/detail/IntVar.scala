

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
sealed abstract class IntVar {
    def Copy: IntVar
    def isZero: Boolean
    def postDec: Boolean
}


private[hano]
object IntVar {

    case object Inf extends IntVar {
        override def Copy: IntVar = this
        override def isZero = false
        override def postDec = true
    }

    case class Of(n: Int) extends IntVar {
        private[this] var _n = n
        override def Copy: IntVar = new Of(n)
        override def isZero = _n == 0
        override def postDec = { val that = _n != 0; _n -= 1; that }
    }

    implicit def fromInt(from: Int): IntVar = new Of(from)
}
