

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Helps to implement checked Seq as resource.
 */
trait SeqResource[A] extends Seq[A] {

    protected def openResource(f: Reaction[A]): Unit
    protected def closeResource(): Unit

    private[this] var _closed = true

    final override def forloop(f: Reaction[A]) = synchronized {
        if (!_closed) {
            throw new IllegalStateException(toString + " shall be closed before forloop")
        }
        _closed = false
        openResource(f)
    }

    final override def close() = synchronized {
        if (!_closed) {
            closeResource()
            _closed = true
        }
    }
}
