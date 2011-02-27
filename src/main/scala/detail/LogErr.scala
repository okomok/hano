

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
object LogErr {
    def apply(t: Throwable, msg: Any) {
        val d = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date())
        java.lang.System.err.println("[hano][" + msg + "]["+ d + "] " + t)
        t match {
            case _: java.lang.Error => t.printStackTrace()
            case _: SeriousException => t.printStackTrace()
            case _ => ()
        }
    }
}
