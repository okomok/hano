

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok; package hano


trait Forwarder[A] extends Seq[A] with scala.Proxy {
    final override def self: Seq[A] = delegate
    protected def delegate: Seq[A]

    protected def around[B](that: => Seq[B]): Seq[B] = that
    protected def around2[B, C](that: => (Seq[B], Seq[C])): (Seq[B], Seq[C]) = {
        val (l, r) = that
        (around(l), around(r))
    }

    override def close(): Unit = delegate.close()
    override def forloop(f: A => Unit, k: Exit => Unit): Unit = delegate.forloop(f, k)
    override def append[B >: A](that: Seq[B]): Seq[B] = around(delegate.append(that))
    override def merge[B >: A](that: Seq[B]): Seq[B] = around(delegate.merge(that))
    override def map[B](f: A => B): Seq[B] = around(delegate.map(f))
    override def flatMap[B](f: A => Seq[B]): Seq[B] = around(delegate.flatMap(f))
    override def filter(p: A => Boolean): Seq[A] = around(delegate.filter(p))
    override def collect[B](f: PartialFunction[A, B]): Seq[B] = around(delegate.collect(f))
    override def remove(p: A => Boolean): Seq[A] = around(delegate.remove(p))
    override def partition(p: A => Boolean): (Seq[A], Seq[A]) = around2(delegate.partition(p))
    override def scanLeft[B](z: B)(op: (B, A) => B): Seq[B] = around(delegate.scanLeft(z)(op))
    override def scanLeft1[B >: A](op: (B, A) => B): Seq[B] = around(delegate.scanLeft1(op))
    override def tail: Seq[A] = around(delegate.tail)
    override def init: Seq[A] = around(delegate.init)
    override def take(n: Int): Seq[A] = around(delegate.take(n))
    override def drop(n: Int): Seq[A] = around(delegate.drop(n))
    override def slice(from: Int, until: Int): Seq[A] = around(delegate.slice(from, until))
    override def takeWhile(p: A => Boolean): Seq[A] = around(delegate.takeWhile(p))
    override def dropWhile(p: A => Boolean): Seq[A] = around(delegate.dropWhile(p))
    override def span(p: A => Boolean): (Seq[A], Seq[A]) = around2(delegate.span(p))
    override def splitAt(n: Int): (Seq[A], Seq[A]) = around2(delegate.splitAt(n))
    override def step(n: Int): Seq[A] = around(delegate.step(n))
    override def stepTime(i: Long): Seq[A] = around(delegate.stepTime(i))
    override def flatten[B](implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = around(delegate.flatten)
    override def unique: Seq[A] = around(delegate.unique)
    override def uniqueBy(p: (A, A) => Boolean): Seq[A] = around(delegate.uniqueBy(p))
    override def unsplit[B](sep: Seq[B])(implicit pre : Seq[A] <:< Seq[Seq[B]]): Seq[B] = around(delegate.unsplit(sep))
    override def zip[B](that: Seq[B]): Seq[(A, B)] = around(delegate.zip(that))
    override def zipWithIndex: Seq[(A, Int)] = around(delegate.zipWithIndex)
    override def unzip[B, C](implicit pre: Seq[A] <:< Seq[(B, C)]): (Seq[B], Seq[C]) = around2(delegate.unzip)
    override def toResponder: Responder[A] = delegate.toResponder
    override def actor: scala.actors.Actor = delegate.actor
    override def react(f: A => Unit): Seq[A] = around(delegate.react(f))
    override def reactMatch(f: PartialFunction[A, Unit]): Seq[A] = around(delegate.reactMatch(f))
    override def fork(f: Seq[A] => Seq[_]): Seq[A] = around(delegate.fork(f))
    override def duplicate: (Seq[A], Seq[A]) = around2(delegate.duplicate)
    override def break: Seq[A] = around(delegate.break)
    override def takeUntil(that: Seq[_]): Seq[A] = around(delegate.takeUntil(that))
    override def dropUntil(that: Seq[_]): Seq[A] = around(delegate.dropUntil(that))
    override def onHead(f: A => Unit): Seq[A] = around(delegate.onHead(f))
    override def onNth(n: Int)(f: A => Unit): Seq[A] = around(delegate.onNth(n)(f))
    override def onClose(f: => Unit): Seq[A] = around(delegate.onClose(f))
    override def catching(f: PartialFunction[Throwable, Unit]): Seq[A] = around(delegate.catching(f))
    override def using(c: java.io.Closeable): Seq[A] = around(delegate.using(c))
    override def protect: Seq[A] = around(delegate.protect)
    override def adjacent(n: Int): Seq[scala.collection.immutable.IndexedSeq[A]] = around(delegate.adjacent(n))
    override def generate[B](it: scala.collection.Iterable[B]): Seq[B] = around(delegate.generate(it))
    override def replace[B >: A](it: scala.collection.Iterable[B]): Seq[B] = around(delegate.replace(it))
    override def replaceRegion[B >: A](n: Int, m: Int, it: scala.collection.Iterable[B]): Seq[B] = around(delegate.replaceRegion(n, m, it))
    override def indices: Seq[Int] = around(delegate.indices)
    override def shift(k: (=> Unit) => Unit): Seq[A] = around(delegate.shift(k))
    override def shiftReact[B >: A](g: B => (B => Unit) => Unit): Seq[B] = around(delegate.shiftReact(g))
    override def breakable: Seq[(A, Function0[Unit])] = around(delegate.breakable)
}
