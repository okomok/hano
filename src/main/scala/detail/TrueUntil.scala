

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TrueUntil(_n: Int) extends (() => Boolean) {
    private[this] var _i = _n
    override def apply(): Boolean = {
        if (_i == 0) {
            false
        } else {
            _i -= 1
            true
        }
    }
}
