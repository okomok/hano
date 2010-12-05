

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import scala.util.continuations


object Seq extends Conversions with PseudoMethods {

    /**
     * Creates a sequence initially containing the specified elements.
     */
    def apply[A](from: A*): Seq[A] = from

    /**
     * The empty sequence
     */
    val empty: Seq[Nothing] = new Empty()

    /**
     * A single-element sequence
     */
    def single[A](x: A): Seq[A] = new Single(x)

    /**
     * An infinite sequence
     */
    def origin(k: (=> Unit) => Unit): Seq[Unit] = new Origin(k)

    /**
     * Turns into a by-name expression.
     */
    def byName[A](xs: => Seq[A]): Seq[A] = new ByName(xs)

    @Annotation.equivalentTo("from(util.optional(body))")
    def optional[A](body: => A): Seq[A] = from {
        try {
            Some(body)
        } catch {
            case _ => None
        }
    }

//    @Annotation.equivalentTo("from(util.optionalErr(body))")
//    def optionalErr[A](body: => A): Seq[A] = from(util.optionalErr(body))
}


/**
 * Seq sequence, which is built upon asynchronous foreach.
 */
trait Seq[+A] extends java.io.Closeable {


    @Annotation.returnThis
    final def of[B >: A]: Seq[B] = this


// kernel

    /**
     * Should be thread-safe.
     */
    override def close(): Unit = ()

    /**
     * Possibly asynchronous foreach with end reaction.
     * Shall guarantee reactions `f*k?`(in regex) are called in a serialized fashion.
     *
     * @param f reaction for each element
     * @param k reaction for the end of sequence (should not throw)
     */
    def forloop(f: A => Unit, k: Exit => Unit): Unit

    @Annotation.equivalentTo("forloop(f, ())")
    final def foreach(f: A => Unit) = forloop(f, _ => ())

    @Annotation.equivalentTo("foreach(_ => ())")
    final def start(): Unit = foreach(_ => ())

    private[hano]
    final def _for(f: A => Unit): _ForThen = new _ForThen(f)

    private[hano]
    sealed class _ForThen(f: A => Unit) {
        def _andThen(k: Exit => Unit): Unit = forloop(f, k)
    }


// combinator

    def append[B >: A](that: Seq[B]): Seq[B] = new Append[B](this, that)

    @Annotation.aliasOf("append")
    final def ++[B >: A](that: Seq[B]): Seq[B] = append(that)

    def merge[B >: A](that: Seq[B]): Seq[B] = new Merge[B](this, that)

    def map[B](f: A => B): Seq[B] = new Map(this, f)

    def flatMap[B](f: A => Seq[B]): Seq[B] = new FlatMap(this, f)

    def filter(p: A => Boolean): Seq[A] = new Filter(this, p)

    final def withFilter(p: A => Boolean): Seq[A] = filter(p)

    def collect[B](f: PartialFunction[A, B]): Seq[B] = new Collect(this, f)

    def remove(p: A => Boolean): Seq[A] = new Remove(this, p)

    def partition(p: A => Boolean): (Seq[A], Seq[A]) = duplicate match {
        case (xs, ys) => (xs.filter(p), ys.remove(p))
    }

    def scanLeft[B](z: B)(op: (B, A) => B): Seq[B] = new ScanLeft(this, z, op)

    def scanLeft1[B >: A](op: (B, A) => B): Seq[B] = new ScanLeft1(this, op)

    final def scanl[B](z: B): _ScanlBy[B] = new _ScanlBy(z)
    sealed class _ScanlBy[B](z: B) {
        @Annotation.equivalentTo("scanLeft(z)(op)")
        def by(op: (B, A) => B): Seq[B] = scanLeft(z)(op)
    }

//    def head: A = throw new UnsupportedOperationException("Seq.head")

    def tail: Seq[A] = new Tail(this)

    def init: Seq[A] = new Init(this)

    def take(n: Int): Seq[A] = new Take(this, n)

    def drop(n: Int): Seq[A] = new Drop(this, n)

    def slice(n: Int, m: Int): Seq[A] = new Slice(this, n, m)

    def takeWhile(p: A => Boolean): Seq[A] = new TakeWhile(this, p)

    def dropWhile(p: A => Boolean): Seq[A] = new DropWhile(this, p)

    def span(p: A => Boolean): (Seq[A], Seq[A]) = duplicate match {
        case (xs, ys) => (xs.takeWhile(p), ys.dropWhile(p))
    }

    def splitAt(n: Int): (Seq[A], Seq[A]) = {
        Pre.nonnegative(n, "splitAt")
        duplicate match {
            case (xs, ys) => (xs.take(n), ys.drop(n))
        }
    }

    def flatten[B](implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = new Flatten(pre(this))

    /**
     * Steps by the specified stride.
     */
    def step(n: Int): Seq[A] = new Step(this, n)

    /**
     * Steps by the specified time-span(millisecond).
     */
    def stepTime(i: Long): Seq[A] = new StepTime(this, i)

    /**
     * Removes duplicates using <code>==</code>.
     */
    def unique: Seq[A] = new Unique(this)

    /**
     * Removes duplicates using the predicate.
     */
    def uniqueBy(p: (A, A) => Boolean): Seq[A] = new UniqueBy(this, p)

    /**
     * Flattens <code>vs</code>, each Seq appending <code>sep</code> except the last one.
     */
    def unsplit[B](sep: Seq[B])(implicit pre: Seq[A] <:< Seq[Seq[B]]): Seq[B] = new Unsplit(pre(this), sep)

    def zip[B](that: Seq[B]): Seq[(A, B)] = new Zip(this, that)
    def zipWithIndex: Seq[(A, Int)] = new ZipWithIndex(this)

    def unzip[B, C](implicit pre: Seq[A] <:< Seq[(B, C)]): (Seq[B], Seq[C]) = pre(this).duplicate match {
        case (xs, ys) => (xs.map(_._1), ys.map(_._2))
    }


// conversion

    @Annotation.conversion @Annotation.pre("synchronous")
    def breakOut[To](implicit bf: scala.collection.generic.CanBuildFrom[Nothing, A, To]): To = {
        val b = bf()
        for (x <- this) {
            b += x
        }
        b.result
    }

    @Annotation.conversion
    def toTraversable: scala.collection.Traversable[A] = new ToTraversable(this)

    @Annotation.conversion
    def toIterable: Iterable[A] = new ToIterable(this)

    @Annotation.conversion
    def toResponder: Responder[A] = new ToResponder(this)

    @Annotation.conversion
    final def toCps: A @continuations.cpsParam[Any, Unit] = {
        continuations.shift {
            (cont: A => Any) => foreach(new DiscardValue(cont))
        }
    }

    def actor: scala.actors.Actor = scala.actors.Actor.actor(start)


// misc

    /**
     * Loops with evaluating `f`.
     */
    def react(f: A => Unit): Seq[A] = new React(this, f)

    /**
     * Loops with evaluating `f`.
     */
    def reactMatch(f: PartialFunction[A, Unit]): Seq[A] = new ReactMatch(this, f)

    @Annotation.equivalentTo("react(_ => f)")
    final def doing(f: => Unit): Seq[A] = react(_ => f)

    /**
     * Forks.
     */
    def fork(f: Seq[A] => Seq[_]): Seq[A] = new Fork(this, f)

    /**
     * Creates a duplicate.
     */
    def duplicate: (Seq[A], Seq[A]) = { val b = new Duplicate(this); (b, b) }

    /**
     * Skips trailing forks.
     */
    def break: Seq[A] = new Break(this)

    /**
     * Takes elements until `that` starts. `that` may be closed.
     */
    def takeUntil(that: Seq[_]): Seq[A] = new TakeUntil(this, that)

    /**
     * Drops elements until `that` starts. `that` may be closed.
     */
    def dropUntil(that: Seq[_]): Seq[A] = new DropUntil(this, that)

    /**
     * Calls `f` on the head of sequence.
     */
    def onHead(f: A => Unit): Seq[A] = new OnHead(this, f)

    /**
     * Calls `f` on the nth of sequence.
     */
    def onNth(n: Int)(f: A => Unit): Seq[A] = new OnNth(this, n, f)

    /**
     * Calls `f` on the closing of sequence.
     */
    def onClose(f: => Unit): Seq[A] = new OnClose(this, f)

    /**
     * Calls `f` on the exit of sequence.
     */
    def onExit(k: Exit => Unit): Seq[A] = new OnExit(this, k)

    /**
     * Pseudo catch-statement
     */
    def catching(f: PartialFunction[Throwable, Unit]): Seq[A] = new Catching(this, f)

    /**
     * Attach a resource.
     */
    def using(c: java.io.Closeable): Seq[A] = new Using(this, c)

    @Annotation.aliasOf("using(this)")
    final def used: Seq[A] = using(this)

    /**
     * Ignores `close()` call.
     */
    def protect: Seq[A] = new Protect(this)

    /*
     * Tokenized by Peg.
    def tokenize[B >: A](p: Peg[B]): Seq[Vector[B]] = new Tokenize[B](this, p)
     */

    /**
     * Retrieves adjacent sequences.
     */
    def adjacent(n: Int): Seq[scala.collection.immutable.IndexedSeq[A]] = new Adjacent(this, n)

    /**
     * Replaces elements by those of `it`. The length of this sequence never becomes longer.
     */
    def generate[B](it: util.Iter[B]): Seq[B] = new Generate(this, it)

    /**
     * Replaces elements by those of `it`. The length of this sequence never be changed.
     */
    def replace[B >: A](it: util.Iter[B]): Seq[B] = new Replace[B](this, it)

    /**
     * Replaces elements by those of `it`. The length of this sequence never be changed.
     */
    def replaceRegion[B >: A](n: Int, m: Int, it: util.Iter[B]): Seq[B] = new ReplaceRegion[B](this, n, m, it)

    @Annotation.equivalentTo("replace(Stream.from(0))")
    def indices: Seq[Int] = new Indices(this)

    /**
     * Reactions are invoked in somewhere you specify.
     */
    def shift(k: (=> Unit) => Unit): Seq[A] = new Shift(this, k)

    /**
     * Reactions are invoked by somehow you specify.
     */
    def shiftReact[B >: A](g: B => (B => Unit) => Unit): Seq[B] = new ShiftReact[B](this, g)

    /**
     * Elements with a break function.
     */
    def breakable: Seq[(A, Function0[Unit])] = new Breakable(this)

}
