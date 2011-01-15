

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Generator extends detail.GeneratorCommon {

    override def iterator[A](xs: Seq[A]): Iterator[A] = detail.BlockingGenerator.iterator(xs)

}
