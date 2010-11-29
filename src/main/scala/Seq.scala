

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


object Seq


trait Seq[+A] extends { self =>

    def forloop(f: A => Any, q: Exit => Any)

    final def foreach(f: A => Any) = forloop(f, _ => ())

}
