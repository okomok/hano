

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


class DragDropTest extends
//    NotFestSuite
    FestTestNGSuite
{
    private var fixt: FrameFixture = null

    override protected def onSetUp {
        val jf = hano.InEdt.future {
            val jf = new swing.JFrame("DragDropTest")
            val jl = new swing.JLabel("Drag")
            jl.setName("Drag")
            jf.getContentPane.add(jl)

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


/*
            hano.block {
                val mouse = hano.Swing.Mouse(jl)
                mouse.Pressed.options.! match {
                    case Some(p) => {
                        println("pressed at: " + (p.getX, p.getY))
                        mouse.Dragged.stepTime(100).takeUntil(mouse.Released).options.! match {
                            case Some(d) => println("dragging at: " + (d.getX, d.getY))
                            case None => ()
                        }
                    }
                    case None => println("released")
                }
            }
*/
/*
            hano.block {
                val mouse = hano.Swing.Mouse(jl)
                for (p <- mouse.Pressed.!?) {
                    println("pressed at: " + (p.getX, p.getY))
                    for (d <- mouse.Dragged.stepTime(100).takeUntil(mouse.Released).!?) {
                        println("dragging at: " + (d.getX, d.getY))
                    }
                    println("released")
                }
            }
*/
            jf
        }

        fixt = new FrameFixture(robot, jf())
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
