

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class ByName[A](_1: () => Seq[A]) extends SeqResource[A] {
    private[this] var _xs: Seq[A] = null
    override val context = async
    override def closeResource() { _xs.close(); _xs = null }
    override def openResource(f: Reaction[A]) {
        assert(_xs == null)
        def _k(q: Exit) { close(); f.exit(q) }

        _xs = _1()

        _xs shift {
            context
        } onEach {
            f(_)
        } onExit {
            _k(_)
        } start()
    }
}
