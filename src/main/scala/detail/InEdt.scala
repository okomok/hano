

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class InEdt() extends Context {
    override def exit(q: Exit) = ()
    override def forloop(f: Reaction[Unit]) {
        javax.swing.SwingUtilities.invokeLater {
            new Runnable {
                override def run() {
                    try {
                        Context.self.forloop(f)
                    } catch {
                        case t: Throwable => LogErr(t, "Reaction.apply error in edt context")
                    }
                }
            }
        }
    }
}
