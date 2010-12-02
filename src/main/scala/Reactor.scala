

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok
package hano


import java.util.concurrent.CopyOnWriteArrayList
import scala.actors.Actor
import scala.collection.JavaConversions._


/**
 * The Seq Actor
 */
trait Reactor extends Actor {
    /**
     * Override to build up a Seq.
     */
    protected def startReactive(r: Seq[Any]): Unit

    private var _f: Any => Unit = { _ => () } // primary
//    private var _k: eval.ByName[Unit] = eval.ByName(())
    private val _fs = new CopyOnWriteArrayList[Any => Unit] // secondaries
//    private val _ks = new CopyOnWriteArrayList[eval.ByName[Unit]]

    final override def act = {
        Actor.loop {
            react {
                case Reactor.Exit => {
//                    _k()
//                    iterative.from(_ks).foreach{k => k()}
                    Actor.exit
                }
                case x => {
                    _f(x)
                    _fs.foreach(_(x))
                }
            }
        }
    }

    final override def start = {
        startReactive(new Reactor.Primary(this))
        super.start
    }

    final override def restart = {
        startReactive(new Reactor.Primary(this))
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
            override protected def startReactive(r: Seq[Any]) = f(r)
        }
        a.start
        a
    }

    /**
     * Constructs a single-threaded Reactor.
     */
    def singleThreaded(f: Seq[Any] => Unit = Starter): Reactor = {
        val a = new Reactor {
            override protected def startReactive(r: Seq[Any]) = f(r)
            override def scheduler = new scala.actors.scheduler.SingleThreadedScheduler
        }
        a.start
        a
    }

    private object Starter extends (Seq[Any] => Unit) {
        override def apply(r: Seq[Any]) = r.start
    }

    private final class Wrap(f: Any => Unit) extends (Any => Unit) {
        override def apply(x: Any) = f(x)
    }

    private class Primary(_1: Reactor) extends Seq[Any] {
        override def forloop(f: Any => Unit, k: Exit => Unit) {
            _1._f = f
//            _1._k = eval.ByName(k)
        }
    }

    private[hano] class Secondary(_1: Reactor) extends Resource[Any] {
        private[this] var _f: Any => Unit = null
//        private[this] var _k: eval.ByName[Unit] = null
        override protected def closeResource() {
            _1._fs.remove(_f)
//            _1._ks.remove(_k)
        }
        override protected def openResource(f: Any => Unit, k: Exit => Unit) {
            _f = new Wrap(f)
//            _k = eval.ByName(k)
            _1._fs.add(_f)
//            _1._ks.add(_k)
        }
    }

}
