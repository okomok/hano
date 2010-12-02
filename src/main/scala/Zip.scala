

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.LinkedList


private class Zip[A, B](_1: Seq[A], _2: Seq[B]) extends Seq[(A, B)] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: Tuple2[A, B] => Unit, k: Exit => Unit) {
        var ends1 = false
        var ends2 = false
        val q1 = new LinkedList[A]
        val q2 = new LinkedList[B]
        val lock = new AnyRef{}
        var kDone = false
        def _k(q: Exit) = if (!kDone) { kDone = true; k(q); close() }
        def invariant = assert(q1.isEmpty || q2.isEmpty)

        _1 _for { x =>
            lock.synchronized {
                invariant
                if (!kDone) {
                    if (q2.isEmpty) {
                        q1.add(x)
                    } else {
                        f(x, q2.poll)
                    }
                }
            }
        } _then { q =>
            lock.synchronized {
                invariant
                ends1 = true
                if (ends2 || q1.isEmpty) {
                    _k(q)
                }
            }
        }

        _2 _for { y =>
            lock.synchronized {
                invariant
                if (!kDone) {
                    if (q1.isEmpty) {
                        q2.add(y)
                    } else {
                        f(q1.poll, y)
                    }
                }
            }
        } _then { q =>
            lock.synchronized {
                invariant
                ends2 = true
                if (ends1 || q2.isEmpty) {
                    _k(q)
                }
            }
        }
    }
}
