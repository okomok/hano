

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait SamplerProxy[-A] extends Sampler[A] with scala.Proxy {
    def self: Sampler[A]

    override def apply(x: A): Sampler.Result = self.apply(x)
    override def not: Sampler[A] = self.not
    override def and[B <: A](that: Sampler[B]): Sampler[B] = self.and(that)
    override def or[B <: A](that: Sampler[B]): Sampler[B] = self.or(that)
}
