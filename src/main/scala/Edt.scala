

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Event-dispatch-thread context
 */
object Edt extends Context {
    override def close() = ()

    override def forloop(f: Reaction[Unit]) {
        javax.swing.SwingUtilities.invokeLater {
            new Runnable {
                override def run() {
                    try {
                        Self.forloop(f)
                    } catch {
                        case t: Throwable => detail.LogErr(t, "Reaction.apply error in Edt context")
                    }
                }
            }
        }
    }
}
