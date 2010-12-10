

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.LinkedList


private class Zip[A, B](_1: Seq[A], _2: Seq[B]) extends Seq[(A, B)] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: Tuple2[A, B] => Unit, k: Exit => Unit) {
        var exit1: Exit = null
        var exit2: Exit = null
        val q1 = new LinkedList[A]
        val q2 = new LinkedList[B]
        val lock = new AnyRef{}
        val _k = CallOnce[Exit] { q => k(q);close() }
        def invariant = assert(q1.isEmpty || q2.isEmpty)

        def zipExit(p: Exit, q: Exit): Exit = {
            import Exit._
            (p, q) match {
                case (p, null) => p
                case (null, q) => q
                case (End, End) => End
                case (Closed, Closed) => Closed
                case (Failed(s), Failed(t)) => Failed((s, t))
                case (Failed(s), _) => Failed(s)
                case (_, Failed(t)) => Failed(t)
                case (Closed, _) => Closed
                case (_, Closed) => Closed
            }
        }

        _1 _for { x =>
            if (!_k.isDone) {
                lock.synchronized {
                    invariant
                    if (q2.isEmpty) {
                        q1.add(x)
                    } else {
                        f(x, q2.poll)
                    }
                }
            }
        } _andThen { q =>
            lock.synchronized {
                invariant
                exit1 = q
                if (exit2 != null || q1.isEmpty) {
                    _k(zipExit(exit1, exit2))
                }
            }
        }

        _2 _for { y =>
            if (!_k.isDone) {
                lock.synchronized {
                    invariant
                    if (q1.isEmpty) {
                        q2.add(y)
                    } else {
                        f(q1.poll, y)
                    }
                }
            }
        } _andThen { q =>
            lock.synchronized {
                invariant
                exit2 = q
                if (exit1 != null || q2.isEmpty) {
                    _k(zipExit(exit1, exit2))
                }
            }
        }
    }
}
