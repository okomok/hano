

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.{Vector => Result}


@deprecated("unused")
object Vector {

    def apply[A](xs: A*): Result[A] = Result.empty ++ xs

    def make[A](xs: hano.Seq[A]): Result[A] = {
        val that = new ArrayBuffer[A]
        for (x <- xs) {
            that += x
        }
        Result.empty ++ that
    }

    def emptyOf[A]: Result[A] = Result.empty

}
