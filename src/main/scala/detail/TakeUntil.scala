

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
class TakeUntil[A](_1: Seq[A], _2: Seq[_]) extends Seq[A] {
    private[this] lazy val close2 = _2.close() // for thread-safety
    override def close() = { _1.close(); close2 }
    override def context = _1.context
    override def forloop(f: Reaction[A]) {
        @volatile var go = true
        val _k = ExitOnce { q => f.exit(q); close() }

        _2 onEach { _ =>
            go = false
            close2
            // _1.close() would send Exit.Closed to f.
        } start()

        _1 onEach { x =>
            _k.beforeExit {
                if (go) {
                    f(x)
                } else {
                    _k(Exit.End)
                }
            }
        } onExit {
            _k(_)
        } start()

/*      broken under Context.unknown.
        val _k = ExitOnce { q => f.exit(q); close() }

        _2.shift(_1) onEach { y =>
            _k(Exit.End)
        } onExit {
            _k(_)
        } start()

        _1 onEach { x =>
            if (!_k.isExited) {
                f(x)
            } else {
                _k(Exit.End)
            }
        } onExit {
            _k(_)
        } start()
*/
    }
}
