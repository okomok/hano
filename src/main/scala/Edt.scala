

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * The event-dispatch-thread process
 */
object Edt extends Process {
    override def close() = ()

    override def `do`(f: Reaction[Unit]) {
        javax.swing.SwingUtilities.invokeLater {
            new Runnable {
                override def run() {
                    try {
                        Self.`do`(f)
                    } catch {
                        case t: Throwable => detail.Log.err("Edt process", t)
                    }
                }
            }
        }
    }
}
