

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
                    var thrown = false
                    try {
                        f()
                    } catch {
                        case t: Throwable => {
                            thrown = true
                            try {
                                f.exit(Exit.Failed(t))
                            } catch {
                                case t: Throwable => LogErr(t, "Reaction.exit error in Edt context")
                            }
                        }
                    }
                    if (!thrown) {
                        try {
                            f.exit(Exit.End)
                        } catch {
                            case t: Throwable => LogErr(t, "Reaction.exit error in Edt context")
                        }
                    }
                }
            }
        }
    }
}
