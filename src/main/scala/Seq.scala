

// Copyright Shunsuke Sogame 2008-2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Seq


trait Seq[+A] extends { self =>

    def forloop(f: A => Unit, q: Exit => Unit)

    final def foreach(f: A => Unit) = forloop(f, _ => ())

}
