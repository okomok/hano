

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class VarFunc0 {
    private[this] var _f: () => Unit = null

    def apply() {
        if (_f ne null) {
            _f()
        }
    }

    def :=(body: => Unit) {
        _f = () => body
    }
}
