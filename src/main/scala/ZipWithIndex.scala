

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class ZipWithIndex[A](_1: Seq[A]) extends Seq[(A, Int)] {
    override def close() = _1.close()
    override def forloop(f: Tuple2[A, Int] => Unit, k: Exit => Unit) {
        var i = 0
        _1 _for { x =>
            f(x, i)
            i += 1
        } _then {
            k
        }
    }
}
