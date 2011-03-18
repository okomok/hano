

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.concurrent.BlockingQueue
import scala.collection.mutable.Builder


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
    override def await(_timeout: Long = INF): Boolean = self.await(_timeout)
    override def append[B >: A](that: Seq[B]): Seq[B] = around(self.append(that))
    override def appendIf[B >: A](that: Seq[B])(p: Exit.Status => Boolean): Seq[B] = around(self.appendIf(that)(p))
    override def prepend[B >: A](that: Seq[B]): Seq[B] = around(self.prepend(that))
    override def merge[B >: A](that: Seq[B]): Seq[B] = around(self.merge(that))
    override def race[B >: A](that: Seq[B]): Seq[B] = around(self.race(that))
    override def map[B](f: A => B): Seq[B] = around(self.map(f))
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
    override def subseq(iter: Iter[Int]) = around(self.subseq(iter))
    override def step(n: Int): Seq[A] = around(self.step(n))
    override def stepFor(d: Long): Seq[A] = around(self.stepFor(d))
    override def fillTime(d: Long): Seq[Unit] = around(self.fillTime(d))
    override def delay(d: Long): Seq[A] = around(self.delay(d))
    override def timeout(d: Long): Seq[A] = around(self.timeout(d))
    override def flatten[B](implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = around(self.flatten)
    override def unique: Seq[A] = around(self.unique)
    override def uniqueBy(p: (A, A) => Boolean): Seq[A] = around(self.uniqueBy(p))
    override def unsplit[B](sep: Seq[B])(implicit pre : Seq[A] <:< Seq[Seq[B]]): Seq[B] = around(self.unsplit(sep))
    override def zip[B](that: Seq[B]): Seq[(A, B)] = around(self.zip(that))
    override def zipWith[B](iter: Iter[B]): Seq[(A, B)] = around(self.zipWith(iter))
    override def unzip[B, C](implicit pre: Seq[A] <:< Seq[(B, C)]): (Seq[B], Seq[C]) = around2(self.unzip)
    override def breakOut[To](implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): To = self.breakOut
    override def toTraversable: scala.collection.Traversable[A] = self.toTraversable
    override def toIterable(timeout: Long = INF, queue: => BlockingQueue[Any] = Seq.defaultBlockingQueue): Iterable[A] = self.toIterable(timeout, queue)
    override def toIter: Iter[A] = self.toIter
    override def toResponder: Responder[A] = self.toResponder
    override def actor: scala.actors.Actor = self.actor
    override def news[B >: A](z: B): Iterable[B] = self.news(z)
    override def latest(_timeout: Long = INF): Iterable[A] = self.latest(_timeout)
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
    override def buffered[To](n: Int, b: => Builder[A, To] = Seq.defaultBuilder[A]): Seq[To] = around(self.buffered(n, b))
    override def bufferedFor[To](d: Long, b: => Builder[A, To] = Seq.defaultBuilder[A]): Seq[To] = around(self.bufferedFor(d, b))
    override def adjacent[To](n: Int, b: => Builder[A, To] = Seq.defaultBuilder[A]): Seq[To] = around(self.adjacent(n, b))
    override def adjacent2: Seq[(A, A)] = around(self.adjacent2)
    override def pull[B](iter: Iter[B]): Seq[B] = around(self.pull(iter))
    override def replace[B >: A](iter: Iter[B]): Seq[B] = around(self.replace(iter))
    override def replaceRegion[B >: A](n: Int, m: Int, iter: Iter[B]): Seq[B] = around(self.replaceRegion(n, m, iter))
    override def indices: Seq[Int] = around(self.indices)
    override def shift(that: Seq[_]): Seq[A] = around(self.shift(that))
    override def shiftStart(that: Seq[_]): Seq[A] = around(self.shiftStart(that))
    override def cycle: Seq[A] = around(self.cycle)
    override def repeat(n: Int): Seq[A] = around(self.repeat(n))
    override def repeatWhile(p: Option[Exit.Status] => Boolean): Seq[A] = around(self.repeatWhile(p))
    override def retry(n: Int): Seq[A] = around(self.retry(n))
    override def noSuccess: Seq[A] = around(self.noSuccess)
    override def neverFail: Seq[A] = around(self.neverFail)
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
    override def copy[To](b: => Builder[A, To] = Seq.defaultBuilder[A]): Seq[To] = around(self.copy(b))
}
