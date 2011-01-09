
# hano 0.1.0-SNAPSHOT

`hano` is a reactive sequence combinator library:

    hano.Block { * =>
        val mouse = hano.Swing.Mouse(jl)
        for (p <- *.in(mouse.Pressed)) {
            println("pressed at: " + (p.getX, p.getY))
            for (d <- *.in(mouse.Dragged.stepTime(100).takeUntil(mouse.Released))) {
                println("dragging at: " + (d.getX, d.getY))
            }
            println("released")
        }
    }



## Rationale

* No locks
* Everything is sequence.
* Continuations plugin is elective.



## Reactive Sequence

A reactive sequence `hano.Seq` is built upon the famous method `foreach`:

    package hano

    trait Seq[+A] {
        def foreach(f: A => Unit): Unit

        // map, filter etc
    }

Unlike `scala.collection.Traversable`, this `foreach` is allowed to be asynchronous.




## Continuations



## Automatic Resource Management




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
[scala.Responder]: http://scala.sygneca.com/libs/responder "scala.Responder"
[scala.collection.Traversable]: http://www.scala-lang.org/archives/downloads/distrib/files/nightly/docs/library/scala/collection/Traversable.html "scala.collection.Traversable"
[scala-arm]: http://github.com/jsuereth/scala-arm "scala-arm"
[ARM in Java]: http://www.infoq.com/news/2010/08/arm-blocks "Automatic Resource Management in Java"

