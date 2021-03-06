
# hano 0.1.0

`hano` is a reactive sequence combinator library:

    val mouse = hano.Swing.Mouse(jl)
    mouse.Pressed.onEach { p =>
        println("pressed at: " + (p.getX, p.getY))
        mouse.Dragged.stepFor {
            100
        } takeUntil {
            mouse.Released
        } onEach { d =>
            println("dragging at: " + (d.getX, d.getY))
        } onExit { _ =>
            println("released")
        } start
    } start

If you are familliar with [Reactive Extensions],
see [Hano vs Rx Method Table](https://github.com/okomok/hano/wiki/hano-vs-rx-method-table "Hano vs Rx Method Table").



## Rationale

* Minimal locks
* Everything is sequence.
* Continuations plugin is elective.



## Reactive Sequence

`hano.Seq` is essentially built upon the famous method `foreach`:

    package hano

    trait Seq[+A] {
        def foreach(f: A => Unit): Unit

        // map, filter etc
    }

Unlike `scala.collection.Traversable`, this `foreach` is allowed to be asynchronous.



## Setup Dependencies for sbt

Append this in your project definition:

    val hano = "com.github.okomok" % "hano_2.9.0" % "0.1.0"
    val okomokReleases = "okomok releases" at "http://okomok.github.com/maven-repo/releases"



## Links

* [ARM in Java]
* [Reactive Extensions]
* [scala-arm]
* [scala.react]
* [Browse Source]
* [Browse Test Source]
* [The Scala Programming Language]


Shunsuke Sogame <<okomok@gmail.com>>



[MIT License]: http://www.opensource.org/licenses/mit-license.php "MIT License"
[Browse Source]: http://github.com/okomok/hano/tree/master/src/main/scala/ "Browse Source"
[Browse Test Source]: http://github.com/okomok/hano/tree/master/src/test/scala/ "Browse Test Source"
[The Scala Programming Language]: http://www.scala-lang.org/ "The Scala Programming Language"
[scala.react]: http://lamp.epfl.ch/~imaier/ "scala.react"
[Reactive Extensions]: http://msdn.microsoft.com/en-us/devlabs/ee794896.aspx "Reactive Extensions"
[neue cc - Reactive Extensions Introduction]: http://neue.cc/2010/07/28_269.html "neue cc - Reactive Extensions?u?a + ???\?b?h???c?d?a?\"
[scala.Responder]: http://scala.sygneca.com/libs/responder "scala.Responder"
[scala.collection.Traversable]: http://www.scala-lang.org/archives/downloads/distrib/files/nightly/docs/library/scala/collection/Traversable.html "scala.collection.Traversable"
[scala-arm]: http://github.com/jsuereth/scala-arm "scala-arm"
[ARM in Java]: http://www.infoq.com/news/2010/08/arm-blocks "Automatic Resource Management in Java"
