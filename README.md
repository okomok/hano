
# hano 0.1.0-SNAPSHOT

`hano` is a reactive sequence combinator library:

    val mouse = hano.Swing.Mouse(jl)
    mouse.Pressed onEach { p =>
        println("pressed at: " + (p.getX, p.getY))
        mouse.Dragged stepTime {
            100
        } takeUntil {
            mouse.Released
        } onEach { d =>
            println("dragging at: " + (d.getX, d.getY))
        } onEnd {
            println("released")
        } start()
    } start()


The current status is pre-alpha.



## Rationale

* Minimal locks
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



## Rx table

<table border="1" bordercolor="#FFCC00" style="background-color:#FFFFCC" width="400" cellpadding="3" cellspacing="3">
	<tr>
		<td>Rx</td>
		<td>hano</td>
	</tr>

	<tr>
		<td>IObservable</td>
		<td>Seq</td>
	</tr>
	<tr>
		<td>IObserver</td>
		<td>Reaction (rarely used)</td>
	</tr>

	<tr>
		<td>Subscribe</td>
		<td>forloop (rarely used)</td>
	</tr>
	<tr>
		<td>Subscribe(...)</td>
		<td>onEach(f).onExit(g).start()</td>
	</tr>
	<tr>
		<td>Do</td>
		<td>onEach</td>
	</tr>
	<tr>
		<td>Run(...)</td>
		<td>onEach(f).onExit(g).await()</td>
	</tr>
	<tr>
		<td>FromEvent</td>
		<td>listen</td>
	</tr>

	<tr>
		<td>Empty</td>
		<td>Empty</td>
	</tr>
	<tr>
		<td>Never</td>
		<td>Empty.noEnd</td>
	</tr>

	<tr>
		<td>SelectMany</td>
		<td>flatMap</td>
	</tr>
	<tr>
		<td>Zip</td>
		<td>zip</td>
	</tr>
	<tr>
		<td>Merge</td>
		<td>merge</td>
	</tr>
	<tr>
		<td>Join</td>
		<td>???</td>
	</tr>
	<tr>
		<td>Concat</td>
		<td>append or ++</td>
	</tr>
	<tr>
		<td>StartWith</td>
		<td>prepend</td>
	</tr>

	<tr>
		<td>Delay</td>
		<td>???</td>
	</tr>
	<tr>
		<td>Sample</td>
		<td>stepTime</td>
	</tr>
	<tr>
		<td>Throttle</td>
		<td>fillTime</td>
	</tr>
	<tr>
		<td>TimeInverval</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>RemoveTimeInterval</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>Timestamp</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>RemoveTimestamp</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>Timeout</td>
		<td>coming soon</td>
	</tr>

	<tr>
		<td>Publish</td>
		<td>???</td>
	</tr>
	<tr>
		<td>Prune</td>
		<td>???</td>
	</tr>
	<tr>
		<td>Replay</td>
		<td>???</td>
	</tr>

	<tr>
		<td>OnErrorResumeNext</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>Catch</td>
		<td>onFailed</td>
	</tr>
	<tr>
		<td>Finally</td>
		<td>onExit</td>
	</tr>

	<tr>
		<td>Select</td>
		<td>map</td>
	</tr>
	<tr>
		<td>Where</td>
		<td>filter</td>
	</tr>
	<tr>
		<td>Scan</td>
		<td>scanLeft1</td>
	</tr>
	<tr>
		<td>Scan0</td>
		<td>scanLeft</td>
	</tr>
	<tr>
		<td>GroupBy</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>BufferWithCount</td>
		<td>adjacent</td>
	</tr>
	<tr>
		<td>BufferWithTime</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>BufferWithTimeOrCount</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>DistinctUntilChanged</td>
		<td>unique</td>
	</tr>

	<tr>
		<td>Skip</td>
		<td>step</td>
	</tr>
	<tr>
		<td>SkipWhile</td>
		<td>dropWhile</td>
	</tr>
	<tr>
		<td>SkipLast</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>Take</td>
		<td>take</td>
	</tr>
	<tr>
		<td>TakeWhile</td>
		<td>takeWhile</td>
	</tr>
	<tr>
		<td>TakeLast</td>
		<td>coming soon</td>
	</tr>
	<tr>
		<td>TakeUntil</td>
		<td>takeUntil</td>
	</tr>

	<tr>
		<td>ToEnumerable</td>
		<td>toIterable</td>
	</tr>

	<tr>
		<td>Repeat</td>
		<td>loop</td>
	</tr>
	<tr>
		<td>Let</td>
		<td>?</td>
	</tr>
	<tr>
		<td>Switch</td>
		<td>?</td>
	</tr>
	<tr>
		<td>AsObservable</td>
		<td>?</td>
	</tr>

	<tr>
		<td></td>
		<td></td>
	</tr>

</table>



## Links

* [ARM in Java]
* [Reactive Extensions]
* [neue cc - Reactive Extensions Introduction]
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
[neue cc - Reactive Extensions Introduction]: http://neue.cc/2010/07/28_269.html "neue cc - Reactive Extensions入門 + メソッド早見解説表"
[scala.Responder]: http://scala.sygneca.com/libs/responder "scala.Responder"
[scala.collection.Traversable]: http://www.scala-lang.org/archives/downloads/distrib/files/nightly/docs/library/scala/collection/Traversable.html "scala.collection.Traversable"
[scala-arm]: http://github.com/jsuereth/scala-arm "scala-arm"
[ARM in Java]: http://www.infoq.com/news/2010/08/arm-blocks "Automatic Resource Management in Java"
