

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.ConcurrentModificationException


class NotSerializedException[A](reaction: Reaction[A]) extends ConcurrentModificationException("method calls shall be serialized")
class MultipleExitsException[A](reaction: Reaction[A]) extends ConcurrentModificationException("multiple `exit` calls not allowed")
class ApplyAfterExitException[A](reaction: Reaction[A]) extends ConcurrentModificationException("`apply` shall not be called after exit")


/**
 * Kicks non-conforming Seq.
 */
@Annotation.mixin
trait CheckedReaction[-A] extends Reaction[A] {
    protected def checkedApply(x: A): Unit
    protected def checkedExit(q: Exit): Unit

    @volatile private var _ing = false
    private val _exit = {
        detail.IfFirst[Exit] { q =>
            checkedExit(q)
        } Else { _ =>
            throw new MultipleExitsException(this)
        }
    }
    private def _apply(x: A) {
        if (_exit.isSecond) {
            throw new ApplyAfterExitException(this)
        } else {
            checkedApply(x)
        }
    }

    final override def apply(x: A) {
        if (_ing) {
            throw new NotSerializedException(this)
        }
        try {
            _ing = true
            _apply(x)
        } finally {
            _ing = false
        }
    }
    final override def exit(q: Exit) = {
        if (_ing) {
            throw new NotSerializedException(this)
        }
        _exit(q)
    }
}
