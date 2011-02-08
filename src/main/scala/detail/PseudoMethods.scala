

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano
package detail


private[hano]
trait PseudoMethods { self: Seq.type =>

    sealed class _OfVariant[A](_this: Seq[A]) {
        def adapt(body: (Seq[A], Reaction[A]) => Unit): Seq[A] = new Adapt(_this, body)
    }
    implicit def _ofVariant[A](_this: Seq[A]): _OfVariant[A] = new _OfVariant(_this)
/*
    sealed class _OfName[A](_this: => Seq[A]) {
        def byName: Seq[A] = new ByName(_this)
    }
    implicit def _ofName[A](_this: => Seq[A]): _OfName[A] = new _OfName(_this)
*/
    sealed class _OfZip2[T1, T2](_this: Seq[(T1, T2)]) {
        def map2[R](f: (T1, T2) => R): Seq[R] = _this.map{case (v1, v2) => f(v1, v2)}
    }
    implicit def _ofZip2[T1, T2](_this: Seq[(T1, T2)]): _OfZip2[T1, T2] = new _OfZip2(_this)

    sealed class _OfZip3[T1, T2, T3](_this: Seq[((T1, T2), T3)]) {
        def map3[R](f: (T1, T2, T3) => R = (v1: T1, v2: T2, v3: T3) => (v1, v2, v3)): Seq[R] = _this.map{case ((v1, v2), v3) => f(v1, v2, v3)}
    }
    implicit def _ofZip3[T1, T2, T3](_this: Seq[((T1, T2), T3)]): _OfZip3[T1, T2, T3] = new _OfZip3(_this)

    sealed class _OfZip4[T1, T2, T3, T4](_this: Seq[(((T1, T2), T3), T4)]) {
        def map4[R](f: (T1, T2, T3, T4) => R = (v1: T1, v2: T2, v3: T3, v4: T4) => (v1, v2, v3, v4)): Seq[R] = _this.map{case (((v1, v2), v3), v4) => f(v1, v2, v3, v4)}
    }
    implicit def _ofZip4[T1, T2, T3, T4](_this: Seq[(((T1, T2), T3), T4)]): _OfZip4[T1, T2, T3, T4] = new _OfZip4(_this)

    sealed class _OfZip5[T1, T2, T3, T4, T5](_this: Seq[((((T1, T2), T3), T4), T5)]) {
        def map5[R](f: (T1, T2, T3, T4, T5) => R = (v1: T1, v2: T2, v3: T3, v4: T4, v5: T5) => (v1, v2, v3, v4, v5)): Seq[R] = _this.map{case ((((v1, v2), v3), v4), v5) => f(v1, v2, v3, v4, v5)}
    }
    implicit def _ofZip5[T1, T2, T3, T4, T5](_this: Seq[((((T1, T2), T3), T4), T5)]): _OfZip5[T1, T2, T3, T4, T5] = new _OfZip5(_this)
}
