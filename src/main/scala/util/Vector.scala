

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano.util


import scala.collection.immutable.{Vector => Result}
import scala.collection.JavaConversions


object Vector {

    def apply[A](xs: A*): Result[A] = Result.empty ++ xs

    def from[A](that: Result[A]): Result[A] = that

    def from[A](from: scala.collection.TraversableOnce[A]): Result[A] = Result.empty ++ from

    def from[A](from: java.lang.Iterable[A]): Result[A] = {
        import JavaConversions._
        Result.empty ++ from
    }

    def from[A](from: hano.Seq[A]): Result[A] = {
        var that = emptyOf[A]
        for (x <- from) {
            that = that :+ x
        }
        that
    }

    def emptyOf[A]: Result[A] = Result.empty

}
