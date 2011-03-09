

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations


object Seq extends detail.Conversions with detail.PseudoMethods {

    /**
     * Creates a sequence initially containing the specified elements.
     */
    @annotation.equivalentTo("Self.pull(from)")
    def apply[A](from: A*): Seq[A] = from
}


/**
 * Reactive sequence, which is built upon asynchronous foreach.
 */
trait Seq[+A] {


    @annotation.returnThis @inline
    final def of[B >: A]: Seq[B] = this

    @annotation.returnThis @inline
    final def asSeq: Seq[A] = this


// kernel

    /**
     * Process where Reactions are invoked.
     */
    def process: Process

    /**
     * (Possibly) asynchronous foreach with the end reaction.
     */
    def forloop(f: Reaction[A])

    @annotation.equivalentTo("forloop(Reaction(f, _ => ()))")
    def foreach(f: A => Unit) = forloop(Reaction(_ => (), f, Exit.defaultHandler))

    @annotation.equivalentTo("foreach(_ => ())")
    def start() = foreach(_ => ())

    /**
     * Blocks until `onExit` is called.
     */
    def await() = detail.Await(this)


// combinator

    def append[B >: A](that: Seq[B]): Seq[B] = new detail.Append[B](this, that)

    @annotation.aliasOf("append")
    final def ++[B >: A](that: Seq[B]): Seq[B] = append(that)

    def prepend[B >: A](that: Seq[B]): Seq[B] = new detail.Prepend[B](this, that)

    def merge[B >: A](that: Seq[B]): Seq[B] = new detail.Merge[B](this, that)

    def race[B >: A](that: Seq[B]): Seq[B] = new detail.Race[B](this, that)

    def map[B](f: A => B): Seq[B] = new detail.Map(this, f)

    /**
     * Appends `that` on the exit; `that` is always appended.
     */
    def appendOnExit[B >: A](that: Seq[B]): Seq[B] = new detail.AppendOnExit[B](this, that)

    /**
     * Appends `that` in case of a failure.
     */
    def substitute[B >: A](that: PartialFunction[Throwable, Seq[B]]): Seq[B] = new detail.Substitute[B](this, that)

    /**
     * Returns an infinite sequence.
     */
    def flatMap[B](f: A => Seq[B]): Seq[B] = new detail.FlatMap(this, f)

    def filter(p: A => Boolean): Seq[A] = new detail.Filter(this, p)

    final def withFilter(p: A => Boolean): Seq[A] = filter(p)

    def collect[B](f: PartialFunction[A, B]): Seq[B] = new detail.Collect(this, f)

    def remove(p: A => Boolean): Seq[A] = new detail.Remove(this, p)

    def partition(p: A => Boolean): (Seq[A], Seq[A]) = duplicate match {
        case (xs, ys) => (xs.filter(p), ys.remove(p))
    }

    def scanLeft[B](z: B)(op: (B, A) => B): Seq[B] = new detail.ScanLeft(this, z, op)

    def scanLeft1[B >: A](op: (B, A) => B): Seq[B] = new detail.ScanLeft1(this, op)

    @annotation.aliasOf("scanLeft")
    final def scan[B](z: B)(op: (B, A) => B): Seq[B] = scanLeft(z)(op)

    @annotation.aliasOf("scanLeft1")
    final def scan1[B >: A](op: (B, A) => B): Seq[B] = scanLeft1(op)

    def tail: Seq[A] = new detail.Tail(this)

    def init: Seq[A] = new detail.Init(this)

    def take(n: Int): Seq[A] = new detail.Take(this, n)

    def drop(n: Int): Seq[A] = new detail.Drop(this, n)

    def slice(n: Int, m: Int): Seq[A] = new detail.Slice(this, n, m)


    def takeWhile(p: A => Boolean): Seq[A] = new detail.TakeWhile(this, p)

    def dropWhile(p: A => Boolean): Seq[A] = new detail.DropWhile(this, p)

    def span(p: A => Boolean): (Seq[A], Seq[A]) = duplicate match {
        case (xs, ys) => (xs.takeWhile(p), ys.dropWhile(p))
    }

    def splitAt(n: Int): (Seq[A], Seq[A]) = {
        detail.Pre.nonnegative(n, "splitAt")
        duplicate match {
            case (xs, ys) => (xs.take(n), ys.drop(n))
        }
    }

    def flatten[B](implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = new detail.Flatten(pre(this))

    /**
     * Extracts a sub-sequence.
     */
    @annotation.pre("`it` is strictly-increasing")
    def subseq(it: Iter[Int]): Seq[A] = new detail.Subseq(this, it)

    /**
     * Steps by the specified stride.
     */
    def step(n: Int): Seq[A] = new detail.Step(this, n)

    /**
     * Steps by the specified time-span(millisecond).
     */
    def stepTime(i: Long): Seq[A] = new detail.StepTime(this, i)

    /**
     * Calls a reaction in case any element doesn't come in the specified time-span(millisecond).
     */
    def fillTime(i: Long): Seq[Unit] = new detail.FillTime(this, i)

    /**
     * Calls reactions with the specified delay(millisecond).
     */
    def delay(i: Long): Seq[A] = new detail.Delay(this, i)

    /**
     * Fails with `TimeOutException` if the head element doesn't come in time(millisecond).
     */
    def timeout(i: Long): Seq[A] = new detail.Timeout(this, i)

    /**
     * Removes duplicates using <code>==</code>.
     */
    def unique: Seq[A] = new detail.Unique(this)

    /**
     * Removes duplicates using the predicate.
     */
    def uniqueBy(p: (A, A) => Boolean): Seq[A] = new detail.UniqueBy(this, p)

    /**
     * Flattens <code>vs</code>, each Seq appending <code>sep</code> except the last one.
     */
    def unsplit[B](sep: Seq[B])(implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = new detail.Unsplit(pre(this), sep)

    def zip[B](that: Seq[B]): Seq[(A, B)] = new detail.Zip(this, that)

    /**
     * Zips with an `Iterable`. Its length is the minimum of the two.
     */
    def zipWith[B](it: Iter[B]): Seq[(A, B)] = new detail.ZipWith(this, it)

    def unzip[B, C](implicit pre: Seq[A] <:< Seq[(B, C)]): (Seq[B], Seq[C]) = pre(this).duplicate match {
        case (xs, ys) => (xs.map(_._1), ys.map(_._2))
    }


// conversion

    @annotation.conversion
    def breakOut[To](implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): To = Val(copy(bf)).get

    @annotation.conversion @annotation.pre("synchronous")
    def toTraversable: scala.collection.Traversable[A] = new detail.ToTraversable(this)

    @annotation.conversion
    def toIterable: Iterable[A] = new detail.ToIterable(this)

    @annotation.conversion
    def toIter: Iter[A] = Iter.from(toIterable)

    @annotation.conversion
    def toResponder: Responder[A] = new detail.ToResponder(this)

    @annotation.conversion
    final def toCps: A @continuations.cpsParam[Any, Unit] = {
        continuations.shift {
            (cont: A => Any) => foreach(new detail.DiscardValue(cont))
        }
    }

    final def cpsFor = new detail.CpsFor(this)

    @annotation.aliasOf("toCps")
    final def ! : A @continuations.cpsParam[Any, Unit] = toCps

    @annotation.aliasOf("cpsFor")
    final def !? = cpsFor

    def actor: scala.actors.Actor = scala.actors.Actor.actor(start)


// misc

    /**
     * Loops with evaluating `f`.
     */
    def react(f: => Reaction[A]): Seq[A] = new detail.React(this, () => f)

    /**
     * Calls `j` on the entrance of sequence.
     */
    def onEnter(j: Exit => Unit): Seq[A] = new detail.OnEnter(this, j)

    /**
     * Calls `k` on the exit of sequence.
     */
    def onExit(k: Exit.Status => Unit): Seq[A] = new detail.OnExit(this, k)

    /**
     * Calls `k` on the end of sequence.
     */
    def onSuccess(k: => Unit): Seq[A] = new detail.OnSuccess(this, () => k)

    /**
     * Calls `k` on the failure of sequence.
     */
    def onFailure(k: Throwable => Unit): Seq[A] = new detail.OnFailure(this, k)

    /**
     * Loops with evaluating `f`.
     */
    def onEach(f: A => Unit): Seq[A] = new detail.OnEach(this, f)

    /**
     * Loops with evaluating `f`.
     */
    def onEachMatch(f: PartialFunction[A, Unit]): Seq[A] = new detail.OnEachMatch(this, f)

    @annotation.equivalentTo("onEach(_ => f)")
    final def doing(f: => Unit): Seq[A] = onEach(_ => f)

    /**
     * Forks.
     */
    def fork(f: Seq[A] => Unit): Seq[A] = new detail.Fork(this, f)

    /**
     * Creates a duplicate.
     */
    def duplicate: (Seq[A], Seq[A]) = { val b = new detail.Duplicate(this); (b, b) }

    /**
     * Takes elements until `that` starts. `that` may be closed.
     */
    def takeUntil(that: Seq[_]): Seq[A] = new detail.TakeUntil(this, that)

    /**
     * Drops elements until `that` starts. `that` may be closed.
     */
    def dropUntil(that: Seq[_]): Seq[A] = new detail.DropUntil(this, that)

    /**
     * Calls `f` on the head of sequence.
     */
    def onHead(f: Option[A] => Unit): Seq[A] = new detail.OnHead(this, f)

    /**
     * Calls `f` on the last of sequence.
     */
    def onLast(f: Option[A] => Unit): Seq[A] = new detail.OnLast(this, f)

    /**
     * Calls `f` on the nth of sequence.
     */
    def onNth(n: Int)(f: Option[A] => Unit): Seq[A] = new detail.OnNth(this, n, f)

    /**
     * Pseudo catch-statement
     */
    def catching(f: PartialFunction[Throwable, Unit]): Seq[A] = new detail.Catching(this, f)

    /**
     * Event-style handler for elements
     */
    def handleEach(f: A => Boolean): Seq[A] = new detail.HandleEach(this, f)

    /**
     * Event-style handler for Exit message
     */
    def handleExit(k: PartialFunction[Exit.Status, Unit]): Seq[A] = new detail.HandleExit(this, k)

    /**
     * Doesn't pass an exit function.
     */
    def protect: Seq[A] = new detail.Protect(this)

    /**
     * Attach a resource.
     */
    def using(c: => java.io.Closeable): Seq[A] = new detail.Using(this, () => c)

    /**
     * Retrieves adjacent sequences.
     */
    def adjacent(n: Int): Seq[scala.collection.immutable.IndexedSeq[A]] = new detail.Adjacent(this, n)

    /**
     * Replaces elements by those of `it`. The length of this sequence never becomes longer.
     */
    def pull[B](it: Iter[B]): Seq[B] = new detail.Pull(this, it)

    /**
     * Replaces elements by those of `it`. The length of this sequence never be changed.
     */
    def replace[B >: A](it: Iter[B]): Seq[B] = new detail.Replace[B](this, it)

    /**
     * Replaces elements by those of `it`. The length of this sequence never be changed.
     */
    def replaceRegion[B >: A](n: Int, m: Int, it: Iter[B]): Seq[B] = new detail.ReplaceRegion[B](this, n, m, it)

    @annotation.equivalentTo("replace(Stream.from(0))")
    def indices: Seq[Int] = new detail.Indices(this)

    /**
     * Reactions are invoked in the process of `that`.
     */
    def shift(that: Seq[_]): Seq[A] = new detail.Shift(this, that)

    /**
     * `forloop` are invoked in the process of `that`.
     */
    def shiftStart(that: Seq[_]): Seq[A] = new detail.ShiftStart(this, that)

    /**
     * Cycles this sequence indefinitely.
     */
    @annotation.equivalentTo("repeatWhile(_ => true)")
    def loop: Seq[A] = new detail.Loop(this)

    /**
     * Cycles this sequence `n` times.
     */
    def repeat(n: Int): Seq[A] = new detail.Repeat(this, n)

    /**
     * Cycles this sequence while `p` returns `true`.
     */
    def repeatWhile(p: Option[Exit.Status] => Boolean): Seq[A] = new detail.RepeatWhile(this, p)

    /**
     * Retries until this sequence ends successfully.
     */
    def retry(n: Int): Seq[A] = new detail.Retry(this, n)

    /**
     * Ignores `Exit.Success`.
     */
    def noSuccess: Seq[A] = new detail.NoSuccess(this)

    /**
     * Disallows multiple `forloop`s.
     */
    def once: Seq[A] = new detail.Once(this)

    /**
     * Turns an algorithm into a `Option` form.
     */
    def option: Seq[Option[A]] = new detail._Option(this)

    /**
     * Turns an algorithm into a default value form.
     */
    def orElse[B >: A](default: => B): Seq[B] = new detail.OrElse[B](this, () => default)

    /**
     * Turns into never-fail sequence. REMOVE ME.
     */
    def options: Seq[Option[A]] = new detail.Options(this)

    /**
     * Turns into never-fail sequence.
     */
    def eithers: Seq[Either[Exit.Status, A]] = new detail.Eithers(this)

    /**
     * Increases the loop grain-size.
     */
    def amplify(n: Int): Seq[A] = new detail.Amplify(this, n)


// standard algorithms

    def isEmpty: Seq[Boolean] = new detail.IsEmpty(this)

    def length: Seq[Int] = new detail.Length(this)

    final def size: Seq[Int] = length

    def head: Seq[A] = new detail.Head(this)

    def last: Seq[A] = new detail.Last(this)

    def nth(n:Int): Seq[A] = new detail.Nth(this, n)

    def find(p: A => Boolean): Seq[A] = new detail.Find(this, p)

    def count(p: A => Boolean): Seq[Int] = new detail.Count(this, p)

    def forall(p: A => Boolean): Seq[Boolean] = new detail.Forall(this, p)

    def exists(p: A => Boolean): Seq[Boolean] = new detail.Exists(this, p)

    def foldLeft[B](z: B)(op: (B, A) => B): Seq[B] = new detail.FoldLeft(this, z, op)

    def reduceLeft[B >: A](op: (B, A) => B): Seq[B] = new detail.ReduceLeft(this, op)

    final def /:[B](z: B)(op: (B, A) => B): Seq[B] = foldLeft(z)(op)

    @annotation.aliasOf("foldLeft")
    final def fold[B](z: B)(op: (B, A) => B): Seq[B] = foldLeft(z)(op)

    @annotation.aliasOf("reduceLeft")
    final def reduce[B >: A](op: (B, A) => B): Seq[B] = reduceLeft(op)

    def sum[B >: A](implicit num: Numeric[B]): Seq[B] = new detail.Sum(this, num)

    def product[B >: A](implicit num: Numeric[B]): Seq[B] = new detail.Product(this, num)

    def min[B >: A](implicit cmp: Ordering[B]): Seq[A] = new detail.Min(this, cmp)

    def max[B >: A](implicit cmp: Ordering[B]): Seq[A] = new detail.Max(this, cmp)

    def minBy[B >: A](f: A => B)(implicit cmp: Ordering[B]): Seq[A] = new detail.MinBy(this, f, cmp)

    def maxBy[B >: A](f: A => B)(implicit cmp: Ordering[B]): Seq[A] = new detail.MaxBy(this, f, cmp)

    def copy[To](implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): Seq[To] = new detail.Copy(this, bf)

}
