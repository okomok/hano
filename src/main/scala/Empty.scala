

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


private class Empty() extends Seq[Nothing] {
    override def forloop(f: Nothing => Unit, k: Exit => Unit) = k(End)
}
