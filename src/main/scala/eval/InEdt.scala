

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.eval


/**
 * Runs in the event-dispatch-thread.
 */
case class InEdt[R](_1: ByName[R]) extends Function0[R] {
    private[this] val f = new Invoke(_1, r => javax.swing.SwingUtilities.invokeLater(r))
    override def apply = f()
}

object InEdt extends Strategy {
    override def apply[R](f: => R) = new InEdt(f)
}
