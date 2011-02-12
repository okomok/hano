

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import detail.SeriousException


/**
 * Kicks non-conforming Seq.
 */
@annotation.mixin
trait CheckedReaction[-A] extends Reaction[A] {

    protected def checkedApply(x: A): Unit
    protected def checkedExit(q: Exit): Unit

    private[this] lazy val mdf = new detail.Modification("method calls shall be serialized in " + toString) with SeriousException

    private[this] val _exit = {
        detail.IfFirst[Exit] { q =>
            checkedExit(q)
        } Else { _ =>
            throw new java.util.ConcurrentModificationException("multiple `exit` calls not allowed in " + toString) with SeriousException
        }
    }

    private def _apply(x: A) {
        if (_exit.isSecond) {
            throw new java.util.ConcurrentModificationException("`apply` shall not be called after `exit` in " + toString) with SeriousException
        } else {
            checkedApply(x)
        }
    }

    final override def apply(x: A) = mdf { _apply(x) }
    final override def exit(q: Exit) = mdf { _exit(q) }
}
