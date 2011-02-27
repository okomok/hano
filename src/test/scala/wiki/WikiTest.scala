

// Copyright Shunsuke Sogame 2010-2011.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest; package wikitest


import com.github.okomok.hano


class WikiTest
    extends NotSuite {
    //extends org.scalatest.junit.JUnit3Suite {


// Concepts

    trait RandomAccessSequence[A] {
        def apply(i: Int): A
    }


    trait ListSequence[A] {
        def head: A
        def tail: ListSequence[A]
    }


    trait Iterator[A] {
        def hasNext: Boolean
        def next: A
    }

    trait IterableSequence[A] {
        def iterator: Iterator[A]
    }


    trait ReactiveSequence[A] {
        def foreach(f: A => Unit): Unit
    }


// Hot vs Cold

    object ColdPrimes extends ReactiveSequence[Int] {
        override def foreach(f: Int => Unit) {
            f(2); f(3); f(5)
        }
    }

    object HotPrimes extends ReactiveSequence[Int] {
        override def foreach(f: Int => Unit) {
            new Thread {
                override def run {
                    f(2); f(3); f(5)
                }
            } start
        }
    }


// combinators

    class FilterImpl[A](xs: ReactiveSequence[A], p: A => Boolean) extends ReactiveSequence[A] {
        override def foreach(f: A => Unit) {
            xs foreach { x =>
                if (p(x)) {
                    f(x)
                }
            }
        }
    }


// pitfalls

    def last[A](xs: ReactiveSequence[A]): Option[A] = {
        var y: Option[A] = None
        xs foreach { x =>
            y = Some(x)
        }
        y // !??
    }

    def testLast {
        expect(Some(5))(last(ColdPrimes))
//        expect(Some(7))(last(HotPrimes))
    }


// variation

    trait ReactiveSequenceEx1[A] {
        def foreachEx(f: A => Unit, onEnd: => Unit): Unit
    }

    trait ReactiveSequenceEx2[A] {
        def foreachEx(f: A => Unit, onError: Throwable => Unit): Unit
    }


    trait Reaction[A] {
        def apply(x: A): Unit
        def onEnd(): Unit
        def onError(why: Throwable): Unit
    }

    trait ReactiveSequenceEx3[A] {
        def foreachEx(f: Reaction[A]): Unit
    }


    trait Exit
    case object End extends Exit
    case class Error(why: Throwable) extends Exit

    trait ReactiveSequenceEx4[A] {
        def foreachEx(f: A => Unit, onExit: Exit => Unit): Unit
    }


// CPS

    import scala.util.continuations.{reset, shift, suspendable}

    def testCps {
        reset {
            val p: Int @suspendable = shift { f => f(2);f(3);f(5) }
            val q: Int @suspendable = shift { f => f(2);f(3);f(5) }
            println((p, q)) // (2,2) (2,3) (2,5) (3,2) ...
        }
    }

    def testCpsReactive {
        def myShift[A](_foreach: (A => Unit) => Unit) = new ReactiveSequence[A] {
            override def foreach(f: A => Unit) = _foreach(f)
        }

        for {
            p <- myShift[Int] { f => f(2);f(3);f(5) }
            q <- myShift[Int] { f => f(2);f(3);f(5) }
        } {
            println((p, q))
        }

        // `for` translation
        myShift[Int]{ f => f(2);f(3);f(5) }.foreach { p =>
            myShift[Int]{ f => f(2);f(3);f(5) }.foreach { q =>
                println((p, q))
            }
        }
    }


    def toCps[A](from: ReactiveSequence[A]): A @suspendable = shift { (f: A => Unit) => from.foreach(f) }

    def testCpsVsFor {
        reset {
            val p = toCps(HotPrimes)
            val q = toCps(ColdPrimes)
            println((p, q))
        }

        for {
            p <- HotPrimes
            q <- ColdPrimes
        } {
            println((p, q))
        }
    }


// ARM

    class MyFile extends java.io.Closeable {
        def write(i: Int) = println("write")
        override def close() = println("close")
    }

    class UseMyFile extends ReactiveSequence[MyFile] {
        override def foreach(f: MyFile => Unit) {
            val file = new MyFile
            try {
                f(file)
            } finally {
                file.close()
            }
        }
    }

    def testArm {
        for (file <- new UseMyFile) {
            file.write(7)
        }

        // `for` translation
        new UseMyFile().foreach { file =>
            file.write(7)
        }
    }


    def testScope {
        reset {
            val file = toCps(new UseMyFile)
            file.write(7)
        }
    }


// Actor

    import scala.actors.Actor

    class ActiveSequence extends ReactiveSequence[Any] {
        var reaction: Any => Unit = null
        object actor extends Actor {
            override def act() {
                Actor.loop {
                    react {
                        case x => reaction(x)
                    }
                }
            }
        }

        override def foreach(f: Any => Unit) {
            reaction = f
            actor.start
        }
    }

    def testActor {
        val xs = new ActiveSequence()
        for {
            p <- xs
        } {
            println(p)
        }

        xs.actor ! 2
        xs.actor ! 3
        xs.actor ! 5
    }


// Swing

    import java.awt.event.{ActionEvent, ActionListener}

    type ActionEventSource = {
        def addActionListener(l: ActionListener)
        def removeActionListener(l: ActionListener)
    }

    class ActionPerformed(source: ActionEventSource) extends ReactiveSequence[ActionEvent] {
        private var l: ActionListener = null
        override def foreach(f: ActionEvent => Unit) {
            l = new ActionListener {
                override def actionPerformed(e: ActionEvent) = f(e)
            }
            source.addActionListener(l)
        }
    }

}


// IO Monad

class IOMonadTezt {// extends org.scalatest.junit.JUnit3Suite {

    trait ReactiveSequence[A] {
        def foreach(f: A => Unit): Unit
    }

    class Println(str: String) extends ReactiveSequence[Unit] {
        override def foreach(f: Unit => Unit) {
            f(println(str))
        }
    }

    object GetLine extends ReactiveSequence[String] {
        import java.io._
        private val stdin = new BufferedReader(new InputStreamReader(java.lang.System.in))
        override def foreach(f: String => Unit) {
            f(stdin.readLine)
        }
    }

    def testIOMonad {
        for {
            _ <- new Println("What's your name?")
            str <- GetLine
            _ <- new Println("Hello, " + str)
        } {
            // start the evaluation
        }
    }

}
