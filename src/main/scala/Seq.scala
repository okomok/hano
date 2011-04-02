

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.concurrent.BlockingQueue
import scala.util.continuations
import scala.collection.mutable.Builder


object Seq extends detail.Conversions with detail.PseudoMethods with detail.Defaults {

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

    @annotation.equivalentTo("onEach(f).start()")
    def foreach(f: A => Unit) = onEach(f).start()

    @annotation.equivalentTo("forloop(new Reaction.Empty)")
    def start() = forloop(new Reaction.End)

    /**
     * Waits and blocks until `onExit` is called.
     */
    def await(implicit _timeout: Util.Default[Long] = NO_TIMEOUT): Boolean = detail.Await(this, _timeout())


// combinator

    def append[B >: A](that: Seq[B]): Seq[B] = new detail.Append[B](this, that)

    /**
     * Appends `that` if `p` returns `true`.
     */
    def appendIf[B >: A](that: Seq[B])(p: Exit.Status => Boolean): Seq[B] = new detail.AppendIf[B](this, that, p)

    @annotation.aliasOf("append")
    final def ++[B >: A](that: Seq[B]): Seq[B] = append(that)

    def prepend[B >: A](that: Seq[B]): Seq[B] = new detail.Prepend[B](this, that)

    def merge[B >: A](that: Seq[B]): Seq[B] = new detail.Merge[B](this, that)

    def race[B >: A](that: Seq[B]): Seq[B] = new detail.Race[B](this, that)

    def map[B](f: A => B): Seq[B] = new detail.Map(this, f)

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

    final def filterNot(p: A => Boolean): Seq[A] = remove(p)

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
        Require.nonnegative(n, "splitAt position")
        duplicate match {
            case (xs, ys) => (xs.take(n), ys.drop(n))
        }
    }

    def flatten[B](implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = new detail.Flatten(pre(this))

    /**
     * Extracts a sub-sequence.
     */
    @annotation.pre("`iter` is strictly-increasing")
    def subseq(iter: Iter[Int]): Seq[A] = new detail.Subseq(this, iter)

    /**
     * Generic filter
     */
    def foldFilter[B](z: B)(p: (B, A) => Option[(B, Boolean)]): Seq[A] = new detail.FoldFilter(this, z, p)

    /**
     * Samples elements such that `iter.next` returns `true`.
     */
    def sample(iter: Iter[Boolean]): Seq[A] = new detail.Sample(this, iter)

    /**
     * Steps by the specified stride.
     */
    def step(n: Int): Seq[A] = new detail.Step(this, n)

    /**
     * Steps by the specified duration(millisecond).
     */
    def stepFor(d: Long): Seq[A] = new detail.StepFor(this, d)

    /**
     * Calls a reaction in case any element doesn't come in the specified duration(millisecond).
     */
    def fillTime(d: Long): Seq[Unit] = new detail.FillTime(this, d)

    /**
     * Calls reactions with the specified delay(millisecond).
     */
    def delay(d: Long): Seq[A] = new detail.Delay(this, d)

    /**
     * Fails with `TimeoutException` if head doesn't come before head of `that`.
     */
    def timeout(that: Seq[_]): Seq[A] = new detail.Timeout(this, that)

    /**
     * Removes adjacent duplicates.
     */
    def unique[B >: A](implicit equ: Equiv[B]): Seq[A] = new detail.Unique(this, equ)

    /**
     * Flattens <code>vs</code>, each Seq appending <code>sep</code> except the last one.
     */
    def unsplit[B](sep: Seq[B])(implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = new detail.Unsplit(pre(this), sep)

    def zip[B](that: Seq[B]): Seq[(A, B)] = new detail.Zip(this, that)

    /**
     * Zips with an `Iterable`. Its length is the minimum of the two.
     */
    def zipWith[B](iter: Iter[B]): Seq[(A, B)] = new detail.ZipWith(this, iter)

    def unzip[B, C](implicit pre: Seq[A] <:< Seq[(B, C)]): (Seq[B], Seq[C]) = pre(this).duplicate match {
        case (xs, ys) => (xs.map(_._1), ys.map(_._2))
    }


// conversion

    @annotation.conversion
    def breakOut[To](implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): To = Val(copy(bf())).get()

    @annotation.conversion @annotation.pre("synchronous")
    def toTraversable: scala.collection.Traversable[A] = new detail.ToTraversable(this)

    @annotation.conversion
    def toIter: Iter[A] = Iter.from(toIterable)

    @annotation.conversion
    def toList: List[A] = breakOut

    @annotation.conversion
    def toResponder: Responder[A] = new detail.ToResponder(this)

    @annotation.conversion
    def toVal[B](implicit pre: Seq[A] <:< Seq[B]): Val[B] = Val(pre(this))

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


// Iterable

    /**
     * Retrieves an `Iterable` from this sequence.
     */
    @annotation.conversion
    def toIterable(implicit timeout: Util.Default[Long] = NO_TIMEOUT, queue: Util.Default.ByName[BlockingQueue[Any]] = Seq.defaultBlockingQueue): Iterable[A] = new detail.ToIterable(this, timeout(), queue())

    /**
     * Retrieves an `Iterator` from this sequence.
     */
    def iterator(implicit timeout: Util.Default[Long] = NO_TIMEOUT, queue: Util.Default.ByName[BlockingQueue[Any]] = Seq.defaultBlockingQueue): Iterator[A] = toIterable(timeout, queue).iterator

    /**
     * Pick up the newest values with the initial value `z`.
     */
    def news[B >: A](z: B): Iterable[B] = new detail.News(this, z)

    /**
     * Pick up the latest values with the initial value `z`.
     */
    def latest(implicit _timeout: Util.Default[Long] = NO_TIMEOUT): Iterable[A] = new detail.Latest(this, _timeout())


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
     * Buffers values.
     */
    def buffered[To](n: Int, b: => Builder[A, To] = Seq.defaultCopyBuilder[A]): Seq[To] = new detail.Buffered(this, n, () => b)

    /**
     * Buffers values within the specified duration(millisecond).
     */
    def bufferedFor[To](d: Long, b: => Builder[A, To] = Seq.defaultCopyBuilder[A]): Seq[To] = new detail.BufferedFor(this, d, () => b)

    /**
     * Retrieves adjacent sequences.
     */
    def adjacent[To](n: Int, b: => Builder[A, To] = Seq.defaultCopyBuilder[A]): Seq[To] = new detail.Adjacent(this, n, () => b)

    /**
     * Retrives adjacent pairs.
     */
    def adjacent2: Seq[(A, A)] = new detail.Adjacent2(this)

    /**
     * Replaces elements by those of `iter`. The length of this sequence never becomes longer.
     */
    def pull[B](iter: Iter[B]): Seq[B] = new detail.Pull(this, iter)

    /**
     * Replaces elements by those of `iter`. The length of this sequence never be changed.
     */
    def replace[B >: A](iter: Iter[B]): Seq[B] = new detail.Replace[B](this, iter)

    /**
     * Replaces elements by those of `iter`. The length of this sequence never be changed.
     */
    def replaceRegion[B >: A](n: Int, m: Int, iter: Iter[B]): Seq[B] = new detail.ReplaceRegion[B](this, n, m, iter)

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

    @annotation.equivalentTo("val p = async; _1.shift(p).shiftStart(p)")
    def asynchronous: Seq[A] = new detail.Asynchronous(this)

    /**
     * Repeats this sequence indefinitely.
     */
    @annotation.equivalentTo("repeatWhile(_ => true)")
    def cycle: Seq[A] = new detail.Cycle(this)

    /**
     * Repeats this sequence `n` times.
     */
    def repeat(n: Int): Seq[A] = new detail.Repeat(this, n)

    /**
     * Repeats this sequence while `p` returns `true`.
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
     * Regards `Exit.Failure` as `Exit.Success`.
     */
    def neverFail: Seq[A] = new detail.NeverFail(this)

    /**
     * Disallows multiple `forloop`s.
     */
    def once: Seq[A] = new detail.Once(this)

    /**
     * Turns an algorithm into a `Option` form.
     */
    def option: Seq[Option[A]] = new detail.SeqOption(this)

    /**
     * Turns an algorithm into a default value form.
     */
    def orElse[B >: A](default: => B): Seq[B] = new detail.OrElse[B](this, () => default)

    /**
     * Increases the cycle grain-size.
     */
    def amplify(n: Int): Seq[A] = new detail.Amplify(this, n)

    /**
     * Materialize.
     */
    def mail: Seq[Mail[A]] = new SeqMail(this)

    /**
     * Dematerialize.
     */
    def unmail[B](implicit pre: Seq[A] <:< Seq[Mail[B]]): Seq[B] = new SeqUnmail(pre(this))


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

    def min[B >: A](implicit ord: Ordering[B]): Seq[A] = new detail.Min(this, ord)

    def max[B >: A](implicit ord: Ordering[B]): Seq[A] = new detail.Max(this, ord)

    def minBy[B >: A](f: A => B)(implicit ord: Ordering[B]): Seq[A] = new detail.MinBy(this, f, ord)

    def maxBy[B >: A](f: A => B)(implicit ord: Ordering[B]): Seq[A] = new detail.MaxBy(this, f, ord)

    def copy[To](b: => Builder[A, To] = Seq.defaultCopyBuilder[A]): Seq[To] = new detail.Copy(this, () => b)

}
