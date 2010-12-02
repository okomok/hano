

// Copyright Shunsuke Sogame 2010.
// Distributed under the terms of an MIT-style license.


package com.github.okomok.hanotest
package festtest
package dragdroptest


import org.testng.annotations._
import org.fest.swing.fixture.FrameFixture
import org.fest.swing.core.ComponentDragAndDrop

import javax.swing

import com.github.okomok.hano
import hano.Seq

import scala.util.continuations


/*
object Rx {

    def block[A](ctx: BlockEnv => A @continuations.cpsParam[A, Any]): Unit = continuations.reset(ctx(new BlockEnv))

    class BlockEnv {
        // loop{...
        def apply[A](ctx: BlockEnv => A @continuations.cpsParam[Any, Any]): hano.Exit @continuations.cpsParam[Any, Any] = {
            println("BlockEnv.apply")
            val c = new BlockEnv
            ctx(c)
            assert(c._xs != null)
            assert(c._f != null)
            new hano.BlockEnv{}.each { new hano.Seq[hano.Exit] {
                override def forloop(f: hano.Exit => Unit, k: hano.Exit => Unit) {
                    println("hey")
                    c._xs.onExit(q => f(q)).foreach(x => c._f(x))
                }
            } }
        }
    }

    class BlockEnv {
        var _xs: hano.Seq[Any] = null
        var _f: Any => Any = null
        // next(...
        def apply[A](xs: hano.Seq[A]): A @continuations.cpsParam[Any, Any] = {
            println("BlockEnv.apply")
            continuations.shift { (k: A => Any) => _xs = xs; _f = k.asInstanceOf[Any => Any] } //xs.foreach(function.discard(k)) }
        }
    }

}
*/

/*
    class Forloop[A](xs: hano.Seq[A]) {
        import hano.Exit
        import hano.BlockEnv.each
        import scala.util.continuations._
        def foreach(g: A => Any @cpsParam[Unit, Any]): Exit @cpsParam[Any, Unit] = each {
            new hano.Seq[Exit] {
                override def forloop(cp: Exit => Unit, k: Exit => Unit) {
                    xs.onExit(q => cp(q)).forloop(x => reset{g(x);()}, k)
                }
            }
        }
    }
*/

class DragDropTest extends
//    NotFestSuite
    FestTestNGSuite
{
    private var fixt: FrameFixture = null

    override protected def onSetUp {
        val jf = hano.eval.InEdt {
            val jf = new swing.JFrame("DragDropTest")
            val jl = new swing.JLabel("Drag")
            jl.setName("Drag")
            jf.getContentPane.add(jl)

            hano.block { * =>
                val mouse = hano.Swing.Mouse(jl)
                for (p <- *(mouse.Pressed)) {
                    println("pressed at: " + (p.getX, p.getY))
                    for (d <- *(mouse.Dragged.stepTime(100).takeUntil(mouse.Released))) {
                        println("dragging at: " + (d.getX, d.getY))
                    }
                    println("released")
                }
            }

/*
            Rx.block { loop =>
                val ex = loop { next =>
                    val mouse = hano.Swing.Mouse(jl)
                    val p = next(mouse.Pressed)
                    println("pressed at: " + (p.getX, p.getY))
                    val ex = loop { next =>
                        val d = next(mouse.Dragged.stepTime(100).takeUntil(mouse.Released))
                        println("dragging at: " + (d.getX, d.getY))
                    }
                    println("released: " + ex)
                    99
                }
                println("hooo: " + ex)
            }
*/
/*
            hano.block { next =>
                val mouse = hano.Swing.Mouse(jl)
                val p = next.head(mouse.Pressed)
                println("pressed at: " + (p.getX, p.getY))
                for (d <- next.until(mouse.Dragged.stepTime(100), mouse.Released)) {
                    println("dragging at: " + (d.getX, d.getY))
                }
                println("released")
                99
            }
*/
            jf
        } apply

        fixt = new FrameFixture(robot, jf)
        fixt.show()
    }

    @Test
    def testTrivial {
        val dd = new ComponentDragAndDrop(robot)
        val jl = fixt.label("Drag").target
        dd.drag(jl, new java.awt.Point(1, 1))
        dd.dragOver(jl, new java.awt.Point(15, 10))
        dd.drop(jl, new java.awt.Point(1, 1))
    }
}