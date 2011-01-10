

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.concurrent.CopyOnWriteArrayList
import scala.actors.Actor


/**
 * An actor built upon Seq
 */
trait Reactor extends Actor {

    /**
     * Override to build up a Seq.
     */
    protected def hanoStart(xs: Seq[Any]): Unit

    private var _f: Reaction[Any] = null // primary
    private val _fs = new CopyOnWriteArrayList[Reaction[Any]] // secondaries

    final override def act = {
        Actor.loop {
            react {
                case Action(f) => f()
                case _: Exit => Actor.exit()
                case x => {
                    if (_f != null) {
                        _f(x)
                    }
                    for (f <- Iter.from(_fs).able) {
                        f(x)
                    }
                }
            }
        }
    }

    final override def exceptionHandler = {
        if (_f != null) {
            case t => _f.exit(Exit.Failed(t))
        } else {
            super.exceptionHandler
        }
    }

    final override def start = {
        hanoStart(new Reactor.Primary(this))
        super.start
    }

    final override def restart = {
        hanoStart(new Reactor.Primary(this))
        super.restart
    }

    final val hanoContext: Context = new detail.Act(this)
}


object Reactor {

    /**
     * Constructs a trivial Reactor.
     */
    def apply(body: Seq[Any] => Unit = Starter): Reactor = {
        val that = new Reactor {
            override protected def hanoStart(xs: Seq[Any]) = body(xs)
        }
        that.start
        that
    }

    /**
     * Constructs a single-threaded Reactor.
     */
    def singleThreaded(body: Seq[Any] => Unit = Starter): Reactor = {
        val that = new Reactor {
            override protected def hanoStart(xs: Seq[Any]) = body(xs)
            override def scheduler = new scala.actors.scheduler.SingleThreadedScheduler
        }
        that.start
        that
    }

    private object Starter extends (Seq[Any] => Unit) {
        override def apply(xs: Seq[Any]) = xs.start
    }

    private class Primary(_1: Reactor) extends Seq[Any] {
        override val context = _1.hanoContext
        override def forloop(f: Reaction[Any]) {
            _1._f = f
        }
    }

    private[hano] class Secondary(_1: Reactor) extends Resource[Any] {
        private[this] var _f: Reaction[Any] = null
        override val context = _1.hanoContext
        override protected def closeResource() {
            _1._fs.remove(_f)
        }
        override protected def openResource(f: Reaction[Any]) {
            _f = new detail.WrappedReaction(f)
            _1._fs.add(_f)
        }
    }
}
