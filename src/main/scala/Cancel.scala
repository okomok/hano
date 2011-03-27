

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Cancellable exit
 */
final class Cancel extends Exit {
    @volatile private[this] var _status: Exit.Status = null
    @volatile private[this] var _exit = Exit.Empty.asExit

    override def apply(q: Exit.Status = Exit.Success) {
        _status = q
        _exit(_status)
    }

    private val toEnterFunction: Exit => Unit = p => {
        _exit = p
        if (_status ne null) {
            _exit(_status)
        }
    }
}

object Cancel {
    implicit def toEnterFunction(from: Cancel): Exit => Unit = from.toEnterFunction
}
