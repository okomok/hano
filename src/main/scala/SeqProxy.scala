

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


trait SeqProxy[+A] extends Seq[A] with scala.Proxy {
    def self: Seq[A]

    protected def around[B](that: => Seq[B]): Seq[B] = that
    protected def around2[B, C](that: => (Seq[B], Seq[C])): (Seq[B], Seq[C]) = {
        val (l, r) = that
        (around(l), around(r))
    }

    override def process: Process = self.process
    override def forloop(f: Reaction[A]) = self.forloop(f)
    override def foreach(f: A => Unit) = self.foreach(f)
    override def start() = self.start()
    override def await() = self.await()
    override def append[B >: A](that: Seq[B]): Seq[B] = around(self.append(that))
    override def prepend[B >: A](that: Seq[B]): Seq[B] = around(self.prepend(that))
    override def merge[B >: A](that: Seq[B]): Seq[B] = around(self.merge(that))
    override def race[B >: A](that: Seq[B]): Seq[B] = around(self.race(that))
    override def map[B](f: A => B): Seq[B] = around(self.map(f))
    override def appendOnExit[B >: A](that: Seq[B]): Seq[B] = around(self.appendOnExit(that))
    override def substitute[B >: A](that: PartialFunction[Throwable, Seq[B]]): Seq[B] = around(self.substitute(that))
    override def flatMap[B](f: A => Seq[B]): Seq[B] = around(self.flatMap(f))
    override def filter(p: A => Boolean): Seq[A] = around(self.filter(p))
    override def collect[B](f: PartialFunction[A, B]): Seq[B] = around(self.collect(f))
    override def remove(p: A => Boolean): Seq[A] = around(self.remove(p))
    override def partition(p: A => Boolean): (Seq[A], Seq[A]) = around2(self.partition(p))
    override def scanLeft[B](z: B)(op: (B, A) => B): Seq[B] = around(self.scanLeft(z)(op))
    override def scanLeft1[B >: A](op: (B, A) => B): Seq[B] = around(self.scanLeft1(op))
    override def tail: Seq[A] = around(self.tail)
    override def init: Seq[A] = around(self.init)
    override def take(n: Int): Seq[A] = around(self.take(n))
    override def drop(n: Int): Seq[A] = around(self.drop(n))
    override def slice(from: Int, until: Int): Seq[A] = around(self.slice(from, until))
    override def takeWhile(p: A => Boolean): Seq[A] = around(self.takeWhile(p))
    override def dropWhile(p: A => Boolean): Seq[A] = around(self.dropWhile(p))
    override def span(p: A => Boolean): (Seq[A], Seq[A]) = around2(self.span(p))
    override def splitAt(n: Int): (Seq[A], Seq[A]) = around2(self.splitAt(n))
    override def subseq(it: Iter[Int]) = around(self.subseq(it))
    override def step(n: Int): Seq[A] = around(self.step(n))
    override def stepTime(i: Long): Seq[A] = around(self.stepTime(i))
    override def fillTime(i: Long): Seq[Unit] = around(self.fillTime(i))
    override def delay(i: Long): Seq[A] = around(self.delay(i))
    override def timeout(i: Long): Seq[A] = around(self.timeout(i))
    override def flatten[B](implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = around(self.flatten)
    override def unique: Seq[A] = around(self.unique)
    override def uniqueBy(p: (A, A) => Boolean): Seq[A] = around(self.uniqueBy(p))
    override def unsplit[B](sep: Seq[B])(implicit pre : Seq[A] <:< Seq[Seq[B]]): Seq[B] = around(self.unsplit(sep))
    override def zip[B](that: Seq[B]): Seq[(A, B)] = around(self.zip(that))
    override def zipWith[B](it: Iter[B]): Seq[(A, B)] = around(self.zipWith(it))
    override def unzip[B, C](implicit pre: Seq[A] <:< Seq[(B, C)]): (Seq[B], Seq[C]) = around2(self.unzip)
    override def breakOut[To](implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): To = self.breakOut
    override def toTraversable: scala.collection.Traversable[A] = self.toTraversable
    override def toIterable: Iterable[A] = self.toIterable
    override def toIter: Iter[A] = self.toIter
    override def toResponder: Responder[A] = self.toResponder
    override def actor: scala.actors.Actor = self.actor
    override def react(f: => Reaction[A]): Seq[A] = around(self.react(f))
    override def onEnter(j: Exit => Unit): Seq[A] = around(self.onEnter(j))
    override def onExit(k: Exit.Status => Unit): Seq[A] = around(self.onExit(k))
    override def onSuccess(k: => Unit): Seq[A] = around(self.onSuccess(k))
    override def onFailure(k: Throwable => Unit): Seq[A] = around(self.onFailure(k))
    override def onEach(f: A => Unit): Seq[A] = around(self.onEach(f))
    override def onEachMatch(f: PartialFunction[A, Unit]): Seq[A] = around(self.onEachMatch(f))
    override def fork(f: Seq[A] => Unit): Seq[A] = around(self.fork(f))
    override def duplicate: (Seq[A], Seq[A]) = around2(self.duplicate)
    override def takeUntil(that: Seq[_]): Seq[A] = around(self.takeUntil(that))
    override def dropUntil(that: Seq[_]): Seq[A] = around(self.dropUntil(that))
    override def onHead(f: Option[A] => Unit): Seq[A] = around(self.onHead(f))
    override def onLast(f: Option[A] => Unit): Seq[A] = around(self.onLast(f))
    override def onNth(n: Int)(f: Option[A] => Unit): Seq[A] = around(self.onNth(n)(f))
    override def catching(f: PartialFunction[Throwable, Unit]): Seq[A] = around(self.catching(f))
    override def handleEach(f: A => Boolean): Seq[A] = around(self.handleEach(f))
    override def handleExit(k: PartialFunction[Exit.Status, Unit]): Seq[A] = around(self.handleExit(k))
    override def protect: Seq[A] = around(self.protect)
    override def using(c: => java.io.Closeable): Seq[A] = around(self.using(c))
    override def adjacent(n: Int): Seq[scala.collection.immutable.IndexedSeq[A]] = around(self.adjacent(n))
    override def pull[B](it: Iter[B]): Seq[B] = around(self.pull(it))
    override def replace[B >: A](it: Iter[B]): Seq[B] = around(self.replace(it))
    override def replaceRegion[B >: A](n: Int, m: Int, it: Iter[B]): Seq[B] = around(self.replaceRegion(n, m, it))
    override def indices: Seq[Int] = around(self.indices)
    override def shift(that: Seq[_]): Seq[A] = around(self.shift(that))
    override def shiftStart(that: Seq[_]): Seq[A] = around(self.shiftStart(that))
    override def loop: Seq[A] = around(self.loop)
    override def loopWhile(p: => Boolean): Seq[A] = around(self.loopWhile(p))
    override def repeat(n: Int): Seq[A] = around(self.repeat(n))
    override def retry(n: Int): Seq[A] = around(self.retry(n))
    override def noSuccess: Seq[A] = around(self.noSuccess)
    override def once: Seq[A] = around(self.once)
    override def option: Seq[Option[A]] = around(self.option)
    override def orElse[B >: A](default: => B): Seq[B] = around(self.orElse(default))
    override def options: Seq[Option[A]] = around(self.options)
    override def eithers: Seq[Either[Exit.Status, A]] = around(self.eithers)
    override def amplify(n: Int): Seq[A] = around(self.amplify(n))
    override def isEmpty: Seq[Boolean] = around(self.isEmpty)
    override def length: Seq[Int] = around(self.length)
    override def head: Seq[A] = around(self.head)
    override def last: Seq[A] = around(self.last)
    override def nth(n: Int): Seq[A] = around(self.nth(n))
    override def find(p: A => Boolean): Seq[A] = around(self.find(p))
    override def count(p: A => Boolean): Seq[Int] = around(self.count(p))
    override def forall(p: A => Boolean): Seq[Boolean] = around(self.forall(p))
    override def exists(p: A => Boolean): Seq[Boolean] = around(self.exists(p))
    override def foldLeft[B](z: B)(op: (B, A) => B): Seq[B] = around(self.foldLeft(z)(op))
    override def reduceLeft[B >: A](op: (B, A) => B): Seq[B] = around(self.reduceLeft(op))
    override def sum[B >: A](implicit num: Numeric[B]): Seq[B] = around(self.sum(num))
    override def product[B >: A](implicit num: Numeric[B]): Seq[B] = around(self.product(num))
    override def min[B >: A](implicit cmp: Ordering[B]): Seq[A] = around(self.min(cmp))
    override def max[B >: A](implicit cmp: Ordering[B]): Seq[A] = around(self.max(cmp))
    override def minBy[B >: A](f: A => B)(implicit cmp: Ordering[B]): Seq[A] = around(self.minBy(f)(cmp))
    override def maxBy[B >: A](f: A => B)(implicit cmp: Ordering[B]): Seq[A] = around(self.maxBy(f)(cmp))
    override def copy[To](implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): Seq[To] = around(self.copy)
}
