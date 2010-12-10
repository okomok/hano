

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Merge[A](_1: Seq[A], _2: Seq[A]) extends Seq[A] {
    override def close() = { _1.close(); _2.close() }
    override def forloop(f: A => Unit, k: Exit => Unit) {
        val _k = CallOnce[Exit] { q => k(q) }
        val _ok = IfFirst[Exit] { _ => () } Else { q => _k(q) }
        val _no = CallOnce[Exit] { q => _k(q);close() }
        val lock = new AnyRef{}

        _1 _for { x =>
            if (!_k.isDone) {
                lock.synchronized {
                    f(x)
                }
            }
        } _andThen {
            case Exit.End =>
                lock.synchronized {
                    _ok(Exit.End)
                }
            case q =>
                lock.synchronized {
                    _no(q)
                }
        }

        _2 _for { y =>
            if (!_k.isDone) {
                lock.synchronized {
                    f(y)
                }
            }
        } _andThen {
            case Exit.End =>
                lock.synchronized {
                    _ok(Exit.End)
                }
            case q =>
                lock.synchronized {
                    _no(q)
                }
        }
    }
}
