

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class InEdt() extends SeqProxy[Unit] {
    override val self = Seq.origin { body =>
        javax.swing.SwingUtilities.invokeLater {
            new Runnable {
                override def run() = body
            }
        }
    }
}
