

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


/**
 * Generic filter
 */
trait Sampler[-A] {
    def apply(x: A): Sampler.Result

    def not: Sampler[A] = new Sampler.Not(this)

    def and[B <: A](that: Sampler[B]): Sampler[B] = new Sampler.And[B](this, that)

    def or[B <: A](that: Sampler[B]): Sampler[B] = new Sampler.Or[B](this, that)
}


object Sampler {

    sealed abstract class Result {
        @annotation.returnThis @inline
        final def asResult: Result = this
    }

    case object Accept extends Result

    case object Reject extends Result

    case object Cancel extends Result

    def range(_start: Int, _end: Int, _step: Int = 1): Sampler[Any] = new Range(_start, _end, _step)

    def from(_start: Int, _step: Int = 1): Sampler[Any] = new From(_start, _step)


    private class Not[A](_1: Sampler[A]) extends Sampler[A] {
        override def apply(x: A): Result = {
            _1(x) match {
                case Accept => Reject
                case Reject | Cancel => Accept
            }
        }
    }

    private class And[A](_1: Sampler[A], _2: Sampler[A]) extends Sampler[A] {
        override def apply(x: A): Result = {
            _1(x) match {
                case Accept => _2(x)
                case other => other
            }
        }
    }

    private class Or[A](_1: Sampler[A], _2: Sampler[A]) extends Sampler[A] {
        override def apply(x: A): Result = {
            _1(x) match {
                case Accept => Accept
                case other => _2(x)
            }
        }
    }

    private class Drop(_1: Int) extends Sampler[Any] {
        private[this] var _i = 0
        private[this] var _r = Reject.asResult

        override def apply(x: Any): Result = {
            if (_i == _1) {
                _r = Accept
            } else {
                _i += 1
            }
            _r
        }
    }

    private class Take(_1: Int) extends Sampler[Any] {
        private[this] var _i = 0
        private[this] var _r = Accept.asResult

        override def apply(x: Any): Result = {
            if (_i == _1) {
                _r = Cancel
            } else {
                _i += 1
            }
            _r
        }
    }

    private class Step(_1: Int) extends Sampler[Any] {
        private[this] var _c = _1
        private[this] var _res = Reject.asResult

        override def apply(x: Any): Result = {
            _res = Reject
            if (_c == _1) {
                _res = Accept
            }
            _c -= 1
            if (_c == 0) {
                _c = _1
            }
            _res
        }
    }

    private class Range(_1: Int, _2: Int, _3: Int) extends SamplerProxy[Any] {
        override val self = new Drop(_1) and new Take(_2 - _1) and new Step(_3)
    }

    private class From(_1: Int, _2: Int) extends SamplerProxy[Any] {
        override val self = new Drop(_1) and new Step(_2)
    }

/*
    private class From(_1: Int, _2: Int) extends Sampler[Any] {
        require(_1 >= 0, "start of `Sampler.from` shall be nonnegative")
        require(_2 > 0, "step of `Sampler.from` shall be positive")

        private[this] var _i = 0
        private[this] var _c = _2
        private[this] var _res = Reject.asResult

        override def apply(x: Any): Result = {
            if (_i == _1) {
                _res = Reject
                if (_c == _2) {
                    _res = Accept
                }
                _c -= 1
                if (_c == 0) {
                    _c = _2
                }
            } else {
                _i += 1
            }

            _res
        }
    }
*/
}
