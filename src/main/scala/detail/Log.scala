

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object Log {

    def apply(tag: Any, msg: Any) {
        val d = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date)
        java.lang.System.err.println("[hano][" + tag + "]["+ d + "] " + msg)
    }

    def err(tag: Any, t: Throwable, rethrow: Boolean = false) {
        apply(tag, t)
        if (IsBuggy(t)) {
            t.printStackTrace()
        }
        if (rethrow) {
            throw t
        }
    }
}
