

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.concurrent.CopyOnWriteArrayList
import scala.actors.Actor


// TODO: Use composition.

/**
 * An actor built upon Seq
 */
trait Reactor extends Actor {
    /**
     * Override to build up a Seq.
     */
    protected def startHano(r: Seq[Any]): Unit

    private var _f: Reaction[Any] = null // primary
    private val _fs = new CopyOnWriteArrayList[Reaction[Any]] // secondaries

    final override def act = {
        Actor.loop {
            react {
                case Reactor.Exit => {
                    Actor.exit
                }
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
        startHano(new Reactor.Primary(this))
        super.start
    }

    final override def restart = {
        startHano(new Reactor.Primary(this))
        super.restart
    }
}


object Reactor {

    /**
     * Message to exit a Reactor.
     */
    object Exit

    /**
     * Message to generate something.
     */
    object Generate

    /**
     * Constructs a trivial Reactor.
     */
    def apply(f: Seq[Any] => Unit = Starter): Reactor = {
        val a = new Reactor {
            override protected def startHano(r: Seq[Any]) = f(r)
        }
        a.start
        a
    }

    /**
     * Constructs a single-threaded Reactor.
     */
    def singleThreaded(f: Seq[Any] => Unit = Starter): Reactor = {
        val a = new Reactor {
            override protected def startHano(r: Seq[Any]) = f(r)
            override def scheduler = new scala.actors.scheduler.SingleThreadedScheduler
        }
        a.start
        a
    }

    private object Starter extends (Seq[Any] => Unit) {
        override def apply(r: Seq[Any]) = r.start
    }

    private class Wrap(f: Reaction[Any]) extends Reaction[Any] {
        override def apply(x: Any) = f(x)
        override def exit(q: Exit) = f.exit(q)
    }

    private class Primary(_1: Reactor) extends Seq[Any] {
        override def context = throw new Error
        override def forloop(f: Reaction[Any]) {
            _1._f = f
        }
    }

    private[hano] class Secondary(_1: Reactor) extends Resource[Any] {
        private[this] var _f: Reaction[Any] = null
        override def context = throw new Error
        override protected def closeResource() {
            _1._fs.remove(_f)
        }
        override protected def openResource(f: Reaction[Any]) {
            _f = new Wrap(f)
            _1._fs.add(_f)
        }
    }

}
