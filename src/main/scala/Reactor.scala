

// Copyright Shunsuke Sogame 2010-2011.
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
    protected def hanoStart(xs: Seq[Any])

    private var _f: Reaction[Any] = null // primary
    private val _fs = new CopyOnWriteArrayList[Reaction[Any]] // secondaries

    final override def act = {
        loop {
            react {
                case Action(f) => f()
                case Close => exit()
                case x => {
                    if (_f ne null) {
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
        if (_f ne null) {
            case t => _f.exit(Exit.Failure(t))
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

    final val hanoProcess = new detail.Async(this).asProcess

    private def _hanoAddPrimary(f: Reaction[Any]) {
        this ! Action {
            f.enter()
        }
        this ! Action {
            _f = f
        }
    }

    private def _hanoAddSecondary(f: Reaction[Any]) {
        this ! Action {
            f.enter {
                Exit { _ =>
                    _fs.remove(f)
                }
            }
        }
        this ! Action {
            _fs.add(f)
        }
    }
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
        override val process = _1.hanoProcess
        override def forloop(f: Reaction[Any]) {
            _1._hanoAddPrimary(f)
        }
    }

    private[hano] class Secondary(_1: Reactor) extends Seq[Any] {
        override val process = _1.hanoProcess
        override def forloop(f: Reaction[Any]) {
            _1._hanoAddSecondary(new detail.WrappedReaction(f))
        }
    }
}
