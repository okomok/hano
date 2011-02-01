

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait SeqAdapter[+A] extends Seq[A] {
    protected def underlying: Seq[_]
    override def close() = underlying.close()
    override def context = underlying.context
}
